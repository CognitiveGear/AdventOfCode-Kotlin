class Day06 : AdventDay(2022, 6) {

    private fun String.packetStart(length: Int) : String {
        return asSequence().windowed(length, 1).indexOfFirst {
            it.toSet().size == length
        }.let { it + length }.toString()
    }

    override fun part1(): String {
        return input.packetStart(4)
    }

    override fun part2(): String {
        return input.packetStart(14)
    }
}

fun main() {
    Day06().main()
}
