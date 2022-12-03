import kotlinx.coroutines.runBlocking
import java.io.File

abstract class AdventDay(day: Int, year: Int) {

    protected val input = runBlocking {
        checkOrGetInput(year, day, File("data"))
    }
    abstract fun part1() : String
    abstract fun part2() : String
    fun run() {
        println(part1())
        println(part2())
    }
}