@file:Suppress("unused")

class Day12 : AdventDay(2022, 12) {

    private val sPos = inputGrid.getPosOf('S').first()
    private val ePos = inputGrid.getPosOf('E').first()
    private val gameGraph =
        InfiniteGraph(
            { current : Point ->
                current.l1Neighbors().filterTo(HashSet()) {
                    inputGrid.contains(it) && inputGrid[current].elevation() <= (inputGrid[it].elevation() + 1)
                }
            },
            { 1 },
            { 0 }
        )

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
        return gameGraph.dijkstra(
            ePos,
        ) { it == sPos }.bestPath.size.let { it - 1 }.toString()
    }

    override fun part2(): String {
        return gameGraph.dijkstra(
            ePos
        ) { inputGrid[it] == 'a' || it == sPos }.bestPath.size.let { it - 1 }.toString()
    }
}

fun main() {
    Day12().main()
}
