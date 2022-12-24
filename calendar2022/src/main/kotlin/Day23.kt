class Day23 : AdventDay(2022, 23) {

    val startElves = lines
        .flatMapIndexed { row, line->
            line.withIndex().filter { it.value == '#' }.map { (col, _) ->
                Point(col, row)
            }
        }.toSet()

    var elfMoved = false

    fun Set<Point>.elfLogic(pos: Point, round: Int) : Point {
        val validMoves = pos.lInfNeighbors().filterTo(HashSet()) { it !in this }
        if (validMoves.size == 8) {
            return pos
        }
        // South and North are inverted from task explanation as we parse the grid in the opposite order
        val checks = listOf(
            pos + Dir.S in validMoves && pos + Dir.SE in validMoves && pos + Dir.SW in validMoves,
            pos + Dir.N in validMoves && pos + Dir.NE in validMoves && pos + Dir.NW in validMoves,
            pos + Dir.W in validMoves && pos + Dir.NW in validMoves && pos + Dir.SW in validMoves,
            pos + Dir.E in validMoves && pos + Dir.NE in validMoves && pos + Dir.SE in validMoves,
        )
        val move = listOf(
            Dir.S,
            Dir.N,
            Dir.W,
            Dir.E
        )
        for (i in 0..3) {
            val index = (i + round) % 4
            if (checks[index]) {
                elfMoved = true
                return pos + move[index]
            }
        }
        return pos
    }

    fun Set<Point>.round(roundNum: Int) : Set<Point> {
        elfMoved = false
        val endPositions = mutableMapOf<Point, Set<Point>>()
        forEach {
            val endElf = elfLogic(it, roundNum)
            endPositions[endElf] = endPositions.getOrDefault(endElf, setOf()) + setOf(it)
        }
        return endPositions.filterValues { it.size <= 1 }.keys +
                endPositions.filterValues { it.size > 1 }.values.flatten()
    }

    fun Set<Point>.boundingBox() : Pair<IntRange, IntRange> {
        val top = maxOf { it.y }
        val bot = minOf { it.y }
        val left = minOf { it.x }
        val right = maxOf { it.x }
        return bot..top to left..right
    }
    fun Pair<IntRange, IntRange>.area() : Int = first.count() * second.count()
    fun Set<Point>.emptyGroundBetween() : Int = (boundingBox().area() - size)

    override fun part1(): String {
        var elves = startElves
        repeat(10) {
            elves = elves.round(it)
        }
        return elves.emptyGroundBetween().toString()
    }

    override fun part2(): String {
        var elves = startElves
        var round = 0
        var next = elves.round(0)
        while (elfMoved) {
            round++
            elves = next
            next = elves.round(round)
        }
        return (round + 1).toString()
    }
}

fun main() {
    Day23().main()
}