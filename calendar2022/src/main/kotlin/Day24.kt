class Day24 : AdventDay( 2022, 24) {


    val maxCol = lines.first().length
    val maxRow = lines.size
    val startPos = Point(lines.first().indexOfFirst { it == '.' }, 0)
    val endPos = Point(lines.last().indexOfFirst { it == '.' }, maxRow - 1)
    val rowRange: IntRange = 1 until (maxRow - 1)
    val colRange: IntRange = 1 until (maxCol - 1)

    fun Pair<Point, Dir>.move() : Pair<Point, Dir> {
        val newX = Math.floorMod(first.x + second.delta.x - 1, maxCol - 2) + 1
        val newY = Math.floorMod(first.y + second.delta.y - 1, maxRow - 2) + 1
        return Point(newX, newY) to second
    }

    val startStorms : Map<Point, List<Dir>> =
        lines.flatMapIndexed { row, line ->
            line.withIndex()
                .filter {
                    it.value != '.' && it.value != '#'
                }.map { (col, char) ->
                    val pos = Point(col, row)
                    when (char) {
                        '<' -> pos to listOf(Dir.W)
                        '>' -> pos to listOf(Dir.E)
                        '^' -> pos to listOf(Dir.S)
                        'v' -> pos to listOf(Dir.N)
                        else -> throw IllegalArgumentException("malformed input")
                    }
                }
        }.toMap()


    val stormState: MutableMap<Int, Map<Point, List<Dir>>> = mutableMapOf(0 to startStorms)

    fun Map<Point, List<Dir>>.next() : Map<Point, List<Dir>> {
        val result = mutableMapOf<Point, List<Dir>>()
        forEach { (key, value) ->
            value.forEach {
                val next = (key to it).move()
                result[next.first] = result.getOrDefault(next.first, listOf()) + listOf(next.second)
            }
        }
        return result
    }

    enum class SnackState {
        FIRST_TREK,
        BACK_TREK,
        SNACK_TREK
    }

    data class Expedition(val pos: Point, val time: Int = 0, val snackState: SnackState = SnackState.FIRST_TREK) {
        override fun toString(): String {
            return "$time:${pos}:${snackState.name}"
        }
    }
    val startExpedition = Expedition(startPos, 0, SnackState.FIRST_TREK)

    fun stormNeighbors(exp: Expedition) : List<Expedition> {
        val futureStorm = stormState.getOrPut(exp.time + 1) {
            stormState[exp.time]!!.next()
        }
        return (listOf(exp.pos) + exp.pos.l1Neighbors())
            .filter {
                (it.x in colRange && it.y in rowRange || it == startPos || it == endPos )
                        && (futureStorm[it]?.isEmpty() ?: true)
            }.map {
                val snackState : SnackState = when(exp.snackState) {
                    SnackState.FIRST_TREK -> {
                        if (it == endPos) {
                            SnackState.BACK_TREK
                        } else {
                            SnackState.FIRST_TREK
                        }
                    }
                    SnackState.BACK_TREK -> {
                        if (it == startPos) {
                            SnackState.SNACK_TREK
                        } else {
                            SnackState.BACK_TREK
                        }
                    }
                    SnackState.SNACK_TREK -> SnackState.SNACK_TREK
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