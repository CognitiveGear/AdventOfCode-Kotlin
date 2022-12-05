class Day01 : AdventDay(1, 2022) {

    private val list = calorieList(input)

    private fun calorieList(input: String): List<Int> {
        return input
            .split("\n\n")
            .map { subString ->
                subString.split('\n')
                    .map {
                        it.toInt()
                    }
            }
            .map { it.sum() }
    }

    override fun part1(): String = list.max().toString()

    override fun part2(): String = list.sorted().takeLast(3).sum().toString()
}

fun main() {
    val day = Day01()
    println(day.part1())
    println(day.part1())
}
