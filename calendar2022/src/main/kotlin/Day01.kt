class Day01 : AdventDay(2022, 1) {

    private val calorieList =
        input.split("\n\n")
            .map { subString ->
                subString.split('\n').map(String::toInt)
            }
            .map(Iterable<Int>::sum)

    override fun part1(): String = calorieList.max().toString()

    override fun part2(): String = calorieList.sorted().takeLast(3).sum().toString()
}

fun main() {
    Day01().main()
}
