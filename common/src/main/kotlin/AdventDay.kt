import kotlinx.coroutines.runBlocking
import java.io.File

abstract class AdventDay(val input: String) {

    constructor(year: Int, day: Int) : this(
        runBlocking {
            checkOrGetInput(year, day, File("data"))
        }
    )

    abstract fun part1() : String
    abstract fun part2() : String

    fun main() {
        println(part1())
        println(part2())
    }
}