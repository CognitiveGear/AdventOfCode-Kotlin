@file:Suppress("unused")

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.naming.TimeLimitExceededException

// Milliseconds after release time that we will wait, before trying to grab input.
private const val WAIT_MIN = 900L
// Will pick and random value from (0, WAIT_MUL) and add that to the wait time above, to limit blocking in case
// other people are running this or similar schemes from similar locations.
private const val WAIT_MUL = 500.0
// Seconds under which we switch into wait and pounce mode.
private const val DELAY_TIME = 100L
// We use the Cayman Islands timezone because it is consistently UTC-5
private const val TIME_ZONE_ID = "America/Cayman"
/**
 * Reads lines from the given input txt file.
 */
fun inputList(name: String) = File("./data", "$name.txt").readLines()

fun inputSequence(name: String, callback: (Sequence<String>) -> Unit) {
    File("./data", "$name.txt")
        .inputStream()
        .bufferedReader()
        .useLines {
            callback(it)
        }
}

suspend fun checkOrGetInput(year: Int, day: Int, dataDir: File) : List<String> {
    val dataFile = File(dataDir, String.format("day%02d.txt", day))
    if (dataFile.exists()) {
        return dataFile.readLines()
    }
    val tokenFile = File(dataDir, "sessionToken.txt")
    if (!tokenFile.exists()) {
        throw FileNotFoundException("You don't have a day input, but you don't have a sessionToken.txt either.")
    }
    val token = tokenFile.readText()
    val est = ZoneId.of(TIME_ZONE_ID)
    val timeNowEST = ZonedDateTime.now().withZoneSameInstant(est)
    val timePuzzle = ZonedDateTime.of(year, 12, day, 0, 0, 0, 0, est)
    val difference = Duration.between(timeNowEST, timePuzzle)
    if (difference.seconds > DELAY_TIME) {
        println(difference.seconds)
        println(DELAY_TIME)
        throw TimeLimitExceededException("You can't time-travel, and it's too soon to wait to download the input.")
    }
    // We're now committed to the download attempt
    println("Attempting to Fetch data...")
    val result: Deferred<String>
    withContext(Dispatchers.IO) {
        result = async {
           AoCWebScraper(token).use {
               it.grabInput(year, day)
           }
        }
    }
    if (difference.seconds > 0) {
        println("Waiting until puzzle is out...")
        delay(1000L * difference.seconds + WAIT_MIN + (Math.random() * WAIT_MUL).toLong())
    }
    val data = result.await()
    val writeJob : Job
    withContext(Dispatchers.IO) {
        writeJob = launch {
            dataFile.writeText(data.dropLastWhile { it == '\n' })
        }
    }
    val lines = data.lines().dropLastWhile { it == "\n" || it == "" }
    writeJob.join()
    return lines
}

class AoCWebScraper(private val sessionToken: String) : Closeable {

    private val client = HttpClient(OkHttp) {
        install(ContentEncoding) {
            deflate()
            gzip()
        }
    }

    @Throws(ResponseException::class)
    suspend fun grabInput(year: Int, day: Int) : String {
        val data : String
        val response = client.get("https://adventofcode.com/$year/day/$day/input") {
            headers {
                append(
                    "User-Agent",
                    "github.com/CognitiveGear/AdventOfCode-Kotlin by cogntive.gear@gmail.com"
                )
                append(
                    "cookie",
                    "session=$sessionToken"
                )
            }
        }
        when (response.status) {
            HttpStatusCode.Accepted, HttpStatusCode.OK -> data = response.body()
            else -> throw ResponseException(response, "AoC:: " + response.body())
        }
        return data
    }
    override fun close() {
        client.close()
    }
}
/**
 * Converts string to md5 hash.
 */
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
