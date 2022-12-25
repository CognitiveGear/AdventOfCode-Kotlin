class Day24 : AdventDay( 2022, 24) {

    val startPos = Point(lines.first().drop(1).indexOfFirst { it == '.' }, -1)
    val endPos = Point(lines.last().drop(1).indexOfFirst { it == '.' }, lines.size - 2)
    val stormGrid = Grid(
        lines.drop(1).dropLast(1).map { line ->
            line.drop(1).dropLast(1).toMutableList()
        }
    )

    enum class SnackState {
        FIRST_TREK,
        BACK_TREK,
        SNACK_TREK
    }

    data class Expedition(val pos: Point, val time: Int = 0, val snackState: SnackState = SnackState.FIRST_TREK) {
        override fun toString(): String = "$time:${pos}:${snackState.name}"
    }
    val startExpedition = Expedition(startPos, 0, SnackState.FIRST_TREK)

    fun check(point: Point, time: Int) : Boolean =
        (stormGrid[point.y, (point.x - time).mod(stormGrid.maxCol)] != '>') &&
        (stormGrid[point.y, (point.x + time).mod(stormGrid.maxCol)] != '<') &&
        (stormGrid[(point.y - time).mod(stormGrid.maxRow), point.x] != 'v') &&
        (stormGrid[(point.y + time).mod(stormGrid.maxRow), point.x] != '^')

    fun stormNeighbors(exp: Expedition) : Sequence<Expedition> {
        return (exp.pos.l1Neighbors() +  sequenceOf(exp.pos))
            .filter {
                (it in stormGrid && check(it, exp.time + 1)) || it == startPos || it == endPos
            }.map {
                val snackState : SnackState = when {
                    exp.snackState == SnackState.FIRST_TREK && it == endPos -> SnackState.BACK_TREK
                    exp.snackState == SnackState.BACK_TREK && it == startPos -> SnackState.SNACK_TREK
                    else -> exp.snackState
                }
                Expedition(it, exp.time + 1, snackState)
            }
    }

    override fun part1(): String {
        val part1Graph = InfiniteGraph(
            ::stormNeighbors,
            { 1 },
            { it.pos l1 endPos }
        )
        val dijkstra = part1Graph.dijkstra(
            startExpedition,
        ) {
            it.pos == endPos
        }
        return dijkstra.best.last().time.toString()
    }

    override fun part2(): String {
        val startToEnd = startPos l1 endPos
        val part2Graph = InfiniteGraph(
            ::stormNeighbors,
            { 1 },
            {
                when (it.snackState) {
                    SnackState.FIRST_TREK -> (it.pos l1 endPos) + 2 * startToEnd
                    SnackState.BACK_TREK -> (it.pos l1 startPos) + startToEnd
                    SnackState.SNACK_TREK -> it.pos l1 endPos
                }
            }
        )
        val dijkstra = part2Graph.dijkstra(
            startExpedition
        ) {
            it.pos == endPos && it.snackState == SnackState.SNACK_TREK
        }
        return dijkstra.best.last().time.toString()
    }
}

fun main() {
    Day24().main()
}