@file:Suppress("unused")

class Day03 : AdventDay("day03") {

    private fun Char.priority(): Int {
        return if (isLowerCase()) {
            this - 'a' + 1
        } else {
            this - 'A' + 27
        }
    }

    private fun List<String>.commonItemPriority(): Int =
        this.map { it.toSet() }.reduce { a, b -> a intersect b }.single().priority()

    override fun part1(): String =
        input.sumOf { elf ->
            elf.chunked(elf.length / 2).commonItemPriority()
        }.toString()

    override fun part2(): String =
        input.chunked(3).sumOf { group ->
            group.commonItemPriority()
        }.toString()
}

fun main() {
    Day03().run()
}