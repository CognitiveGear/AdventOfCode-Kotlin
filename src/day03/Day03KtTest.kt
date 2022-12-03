package day03

import org.junit.jupiter.api.Test

import java.io.File

class Day03KtTest {

    private val testFile = File("./src/day03", "test.txt").readLines()

    @Test
    fun testPart1() {
        val result = part1(testFile)
        println("part1 == $result")
        assert(result == "157")
    }

    @Test
    fun testPart2() {
        val result = part2(testFile)
        println("part2 == $result")
        assert(result == "70")
    }
}