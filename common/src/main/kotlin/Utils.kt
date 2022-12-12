@file:Suppress("unused")

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Duration
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.naming.TimeLimitExceededException

// Milliseconds after release time that we will wait, before trying to grab input.
private const val WAIT_MIN = 900L
// Will pick and random value from (0, WAIT_MUL) and add that to the wait time above, to limit blocking in case
// other people are running this or similar schemes from similar locations.
private const val WAIT_MUL = 500.0
// Seconds under which we switch into wait and pounce mode (default 5 minutes).
private const val DELAY_TIME = 300L
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

fun grabDayInput(year : Int, day: Int) : String =
    runBlocking {
        checkOrGetInput(year, day, File("/data"))
    }

fun String.grabInts() : List<Int> = Regex("""-?\d+""").findAll(this).toList().map { it.value.toInt() }

suspend fun checkOrGetInput(year: Int, day: Int, dataDir: File) : String {
    val dayFileName = String.format("day%02d.txt", day)
    val dataFile = File(dataDir, dayFileName)
    if (dataFile.exists()) {
        return dataFile.readText()
    }
    val tokenFile = File(dataDir, "sessionToken.txt")
    if (!tokenFile.exists()) {
        throw FileNotFoundException("You don't have a day input, but you don't have a sessionToken.txt either.")
    }
    val est = ZoneOffset.ofHours(-5)
    val timeNowEST = ZonedDateTime.now().withZoneSameInstant(est)
    val timePuzzle = ZonedDateTime.of(year, 12, day, 0, 0, 0, 0, est)
    val difference = Duration.between(timeNowEST, timePuzzle)
    if (difference.seconds > DELAY_TIME) {
        throw TimeLimitExceededException("You can't time-travel, and it's too soon to wait to download the input.")
    }
    // We're committed to the download attempt
    println("Fetching...")
    val scraper = AoCWebScraper(tokenFile.readText())
    if (difference.seconds > 0) {
        println("Waiting until puzzle is out...")
        delay(1000L * difference.seconds + WAIT_MIN + (Math.random() * WAIT_MUL).toLong())
    }
    val data = scraper.use {
        it.grabInput(year, day)
    }.dropLastWhile { it == '\n' }
    dataFile.writeText(data)
    return data
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

fun Int.square() = this * this
/**
 * Converts string to md5 hash.
 */
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
