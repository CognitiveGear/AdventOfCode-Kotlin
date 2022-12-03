package day01

import org.junit.jupiter.api.Test
import java.io.File

class Day01KtTest {

    private val calories = calorieList(File("./src/day01/", "test.txt").readLines())

    @Test
    fun testPart1() {
        val result = part1(calories)
        println("part1 == $result")
        assert(result == 24000)
    }

    @Test
    fun testPart2() {
        val result = part2(calories)
        println("part2 == $result")
        assert(result == 45000)
    }
}