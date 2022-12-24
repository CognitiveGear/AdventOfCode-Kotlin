class Day22 : AdventDay(2022, 22) {

    val startPoint = Point(lines.first().indexOfFirst { it == '.' }, 0)
    data class Turtle(val pos: Point, val facing: Dir) {
        fun rotateLeft() : Turtle = Turtle(pos, facing.rotateLeft())
        fun rotateRight() : Turtle = Turtle(pos, facing.rotateRight())
        fun code() : String = (1000L * (-pos.y + 1).toLong() + 4L * (pos.x + 1).toLong() +
                facing.ordinal.toLong()).toString()
    }

    val cardinalDir : List<Dir> = listOf(Dir.E, Dir.S, Dir.W, Dir.N)
    val gameGraph: InfiniteGraph<Turtle>
    val part1Neighbors = mutableMapOf<Turtle, List<Turtle>>()
    val part2Neighbors = mutableMapOf<Turtle, List<Turtle>>()
    val gameMap : MutableMap<Point, Char>
    val sideLen = lines.dropLast(2).minOf { it.count { it != ' ' } }

    init {
        println(sideLen)
        gameMap = mutableMapOf<Point, Char>()
        lines.dropLast(2).forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                when (char) {
                    '.', '#' -> gameMap[Point(col, -row)] = char
                    ' ' -> {}
                }
            }
        }
        gameMap.filterValues { it == '.' }.keys.forEach { pos ->
            cardinalDir.forEach { facing ->
                val current = Turtle(pos, facing)
                val nextPos = pos + facing
                val rotateList = listOf(Turtle(pos, facing.rotateLeft()), Turtle(pos, facing.rotateRight()))
                val char = gameMap[nextPos]
                if (char != null) {
                    if (char == '.') {
                        part1Neighbors[current] = rotateList + Turtle(nextPos, facing)
                        part2Neighbors[current] = rotateList + Turtle(nextPos, facing)
                    } else {
                        part1Neighbors[current] = rotateList
                        part2Neighbors[current] = rotateList
                    }
                } else {
                    val part1WrapPos = when (facing) {
                        Dir.N -> gameMap.keys.filter { it.x == pos.x }.minBy { it.y }
                        Dir.S -> gameMap.keys.filter { it.x == pos.x }.maxBy { it.y }
                        Dir.W -> gameMap.keys.filter { it.y == pos.y }.maxBy { it.x }
                        Dir.E -> gameMap.keys.filter { it.y == pos.y}.minBy { it.x }
                        else -> throw IllegalStateException("Shouldn't be possible...")
                    }
                    val wrapChar = gameMap[part1WrapPos]!!
                    if (wrapChar == '.') {
                        part1Neighbors[current] = rotateList + Turtle(part1WrapPos, facing)
                    } else {
                        part1Neighbors[current] = rotateList
                    }
                    // This cube-wrapping is input dependent
                    val cubeWrap = when {
                        facing == Dir.W && pos.x == 50 && pos.y in 0 downTo -49 ->
                            Turtle(Point(0, -100 - (49 + pos.y)), Dir.E)
                        facing == Dir.W && pos.x == 50 && pos.y in -50 downTo -99 ->
                            Turtle(Point(-(pos.y + 50), -100), Dir.S)
                        facing == Dir.N && pos.y == -100 && pos.x in 0..49 ->
                            Turtle(Point(50, -50 - pos.x), Dir.E)
                        facing == Dir.W && pos.x == 0 && pos.y in -100 downTo -149 ->
                            Turtle(Point(50, -149 - pos.y), Dir.E)
                        facing == Dir.W && pos.x == 0 && pos.y in -150 downTo -199 ->
                            Turtle(Point(-pos.y - 100, 0), Dir.S)
                        facing == Dir.S && pos.x in 0..49 && pos.y == -199 ->
                            Turtle(Point(pos.x + 100, 0), Dir.S)
                        facing == Dir.E && pos.x == 49 && pos.y in -199..-100 ->
                            Turtle(Point(-pos.y - 100, -149), Dir.N)
                        facing == Dir.S && pos.y == -149 && pos.x in 50..99 ->
                            Turtle(Point(49, -100 - pos.x), Dir.W)
                        facing == Dir.E && pos.x == 99 && pos.y in -149..-100 ->
                            Turtle(Point(149, -pos.y - 149), Dir.W)
                        facing == Dir.E && pos.x == 99 && pos.y in -99..-50 ->
                            Turtle(Point(50 - pos.y, -49), Dir.N)
                        facing == Dir.S && pos.y == -49 && pos.x in 100..149 ->
                            Turtle(Point(99, 50 - pos.x), Dir.W)
                        facing == Dir.E && pos.x == 149 && pos.y in -49..0 ->
                            Turtle(Point(99, -149 - pos.y), Dir.W)
                        facing == Dir.N && pos.y == 0 && pos.x in 100..149 ->
                            Turtle(Point(pos.x - 100, -199), Dir.N)
                        facing == Dir.N && pos.y == 0 && pos.x in 50..99 ->
                            Turtle(Point(0, -100 - pos.x), Dir.E)
                        else -> throw IllegalStateException("Shouldn't get here...")
                    }
                    val cubeChar = gameMap[cubeWrap.pos]!!
                    if (cubeChar == '.') {
                        part2Neighbors[current] = rotateList + cubeWrap
                    } else {
                        part2Neighbors[current] = rotateList
                    }
                }
            }
        }
        gameGraph = InfiniteGraph({ part1Neighbors[it]!! }, { 1 })
    }

    val startTurtle = Turtle(startPoint, Dir.E)
    val orders = """\d+|\w""".toRegex().findAll(lines.last())

    fun followMoveOrders(start: Turtle, neighbors: Map<Turtle, List<Turtle>>) : Turtle {
        var turtle = start
        orders.map { it.value }.forEach { order ->
            when (order) {
                "L" -> turtle = turtle.rotateLeft()
                "R" -> turtle = turtle.rotateRight()
                else -> {
                    val number = order.toInt()
                    for (i in 0 until number) {
                        val nextTile = neighbors[turtle]!!.filter { it.pos != turtle.pos }
                        if (nextTile.isEmpty()) {
                            break
                        }
                        turtle = nextTile.first()
                    }
                }
            }
        }
        return turtle
    }

    override fun part1(): String {
        return followMoveOrders(startTurtle, part1Neighbors).code()
    }

    override fun part2(): String {
        return followMoveOrders(startTurtle, part2Neighbors).code()
    }
}

fun main() {
    Day22().main()
}