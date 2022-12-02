package day02

import java.io.File

class Day02KtTest {

    private val lines: List<String> = File("./src/day02/", "test.txt").readLines()

    @org.junit.jupiter.api.Test
    fun testPart1() {
        val result = part1(lines)
        println(result)
        assert(part1(lines) == 15)
    }

    @org.junit.jupiter.api.Test
    fun testPart2() {
        val result = part2(lines)
        println(result)
        assert(result == 12)
    }
}