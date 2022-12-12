@file:Suppress("unused")

import kotlin.time.ExperimentalTime

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

    private fun validNeighbors(current : Point) : Sequence<Point> =
        current.l1Neighbors().filter {
            inputGrid.containsIndex(it) && inputGrid[current].elevation() <= (inputGrid[it].elevation() + 1)
        }

    @OptIn(ExperimentalTime::class)
    override fun part1(): String {
        val dijkstra: Dijkstra<Point> =
            Dijkstra(
                ePos,
                ::validNeighbors,
                { it == sPos },
                vertexCost = { it l1Norm sPos },
            )
        return dijkstra.bestPath.size.let { it - 1 }.toString()
    }

    @OptIn(ExperimentalTime::class)
    override fun part2(): String {
        val dijkstra : Dijkstra<Point> =
            Dijkstra(
                ePos,
                ::validNeighbors,
                { inputGrid[it] == 'a' || it == sPos },
            )
        return dijkstra.bestPath.size.let { it - 1 }.toString()
    }
}

fun main() {
    Day12().main()
}
