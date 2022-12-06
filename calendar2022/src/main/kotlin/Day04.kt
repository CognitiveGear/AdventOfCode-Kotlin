class Day04 : AdventDay(2022, 4) {

    private operator fun IntRange.contains(other: IntRange) = other.first in this && other.last in this

    private infix fun IntRange.overlaps(other: IntRange) = other.first in this || other.last in this

    private fun String.toElf() : IntRange =
        substringBefore('-').toInt()..substringAfter('-').toInt()
    private fun String.toElfPair() : Pair<IntRange, IntRange> =
        substringBefore(',').toElf() to substringAfter(',').toElf()

    override fun part1(): String {
        return input.split('\n').count { line ->
            val (a, b) = line.toElfPair()
            a in b || b in a
        }.toString()
    }

    override fun part2(): String {
        return input.split('\n').count { line ->
            val (a, b) = line.toElfPair()
            a overlaps b || b overlaps a
        }.toString()
    }
}

fun main() {
    Day04().main()
}