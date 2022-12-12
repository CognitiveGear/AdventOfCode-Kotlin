class Day12 : AdventDay(2022, 12) {

    private val sPos = inputGrid.getPosOf('S').first()
    private val ePos = inputGrid.getPosOf('E').first()
    private fun Char.elevation() : Int =
        if (isLowerCase()) {
            this.code - 'a'.code
        } else {
            if (this == 'S') {
                0
            } else {
                'z' - 'a'
            }
        }

    override fun part1(): String {
        return BreadthFirst (
            sPos,
            compareBy { it.size + it.last().l1Norm(ePos) },
            { current ->
                inputGrid.l1Neighbors(current).filter { neighbor ->
                    inputGrid[neighbor].elevation() <= (inputGrid[current].elevation() + 1)
                }
            },
            { it == ePos }
        ).bestPath.size.let { it - 1 }.toString()
    }

    override fun part2(): String {
        return BreadthFirst (
            ePos,
            compareBy { it.size },
            { current ->
                inputGrid.l1Neighbors(current).filter { neighbor ->
                    inputGrid[current].elevation() <= (inputGrid[neighbor].elevation() + 1)
                }
            },
            { inputGrid[it] == 'a' }
        ).bestPath.size.let { it - 1 }.toString()
    }
}

fun main() {
    Day12().main()
}
