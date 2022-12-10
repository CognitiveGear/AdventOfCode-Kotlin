class Day10 : AdventDay(2022, 10) {

    private val xHistory : List<Int> =
        (sequenceOf(0) + input.lineSequence().flatMap { line ->
            if (line[0] == 'n') {
                listOf(0)
            } else {
                listOf(0, line.substring(5).toInt())
            }
        }).runningFold(1) { acc, value ->
            acc + value
        }.toList()

    override fun part1(): String =
        (20..220 step 40).sumOf {
            xHistory[it] * it
        }.toString()

    override fun part2(): String =
        xHistory.chunked(40).dropLast(1).joinToString("\n") { scanLine ->
            scanLine.mapIndexed { index, value ->
                if (index >= value && index <= value + 2) {
                    '█'
                } else {
                    ' '
                }
            }.toCharArray().concatToString()
        }
}

fun main() {
    Day10().main()
}
