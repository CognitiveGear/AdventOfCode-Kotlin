import kotlinx.coroutines.runBlocking
import java.io.File

abstract class AdventDay(val input: String) {

    protected val lines by lazy { input.lines() }
    protected val lineSequence by lazy { input.lineSequence() }
    protected val inputGrid : Grid<Char> by lazy { Grid(lines.map { it.toMutableList() }) }

    constructor(testDay: Int) : this(File("data", "day${testDay}Test.txt").readText())

    constructor(year: Int, day: Int) : this(
        runBlocking {
            checkOrGetInput(year, day, File("data"))
        }
    )

    abstract fun part1() : String
    abstract fun part2() : String

    fun main() {
        val part1Result = part1()
        val part2Result = part2()
        println("part 1 = $part1Result")
        println("part 2 = $part2Result")
    }
}