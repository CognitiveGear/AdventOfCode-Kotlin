class Day25 : AdventDay(2022, 25) {

    fun String.fromSnafu() : Long {
        var result = 0L
        forEach {
            result = result * 5L + when (it) {
                '2' -> 2L
                '1' -> 1L
                '0' -> 0L
                '-' -> -1L
                '=' -> -2L
                else -> throw IllegalArgumentException("Can't parse SNAFU")
            }
        }
        return result
    }

    fun Long.toSnafu() : String {
        var carry = this
        val result = mutableListOf<Char>()
        while (carry != 0L) {
            when (carry.mod(5L)) {
                0L -> result.add('0')
                1L -> result.add('1')
                2L -> result.add('2')
                3L -> {
                    result.add('=')
                    carry += 5L
                }
                4L -> {
                    result.add('-')
                    carry += 5L
                }
            }
            carry /= 5L
        }
        return result.reversed().joinToString("")
    }

    val snafus = lines.map { it.fromSnafu() }

    override fun part1(): String {
        return snafus.sum().toSnafu()
    }

    override fun part2(): String {
        return ""
    }
}

fun main() {
    Day25().main()
}