class Day04 : AdventDay(4, 2022) {

    private infix fun IntRange.contains(other: IntRange) = other.first in this && other.last in this

    private infix fun IntRange.overlaps(other: IntRange) = other.first in this || other.last in this

    private fun String.toElfPair() : List<IntRange> = grabInts().chunked(2).map { it.first()..it.last() }

    override fun part1(): String {
        return input.count { line ->
            val (a, b) = line.toElfPair()
            a contains b || b contains a
        }.toString()
    }

    override fun part2(): String {
        return input.count { line ->
            val (a, b) = line.toElfPair()
            a overlaps b || b overlaps a
        }.toString()
    }
}

fun main() {
    Day04().run()
}