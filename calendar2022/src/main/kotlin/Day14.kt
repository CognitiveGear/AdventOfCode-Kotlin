import kotlin.math.sign

class Day14 : AdventDay(2022, 14) {

    private val gameMap: MutableMap<Point, Char> = mutableMapOf()
    private val voidY : Int
    private val sandTapPos : Point = Point(500, 0)
    private var totalSandBeforeVoid : Int = 0
    private var totalSandAtRest : Int = 0
    private var voidReached: Boolean = false

    private fun drawLine(line: List<Point>) {
        val (start, end) = line
        val diff = (end - start)
        val step = Point(diff.x.sign, diff.y.sign)
        var current = start
        while (current != end) {
            gameMap[current] = '#'
            current += step
        }
        gameMap[end] = '#'
    }

    init {
        val segmentList = lines.map { line ->
            line.split(" -> ").map { pair ->
                Point(pair.substringBefore(',').toInt(), pair.substringAfter(',').toInt())
            }
        }
        val maxY = segmentList.maxOf { segment -> segment.maxOf { it.y }}
        voidY = maxY + 1
        segmentList.forEach {segment ->
            segment.windowed(2, 1).forEach {
                drawLine(it)
            }
        }
        var sandPos = Point(0, 0)
        while (sandPos != sandTapPos) {
            sandPos = sandTapPos
            var sandFalling = true
            while (sandFalling) {
                val validFallTarget =
                    sequenceOf(Point(0, 1), Point(-1, 1), Point(1, 1))
                        .map { it + sandPos }
                        .filter { it !in gameMap && it.y <= voidY }
                        .firstOrNull()
                if (validFallTarget == null) {
                    sandFalling = false
                    gameMap[sandPos] = 'o'
                    totalSandAtRest++
                } else {
                    sandPos = validFallTarget
                    if (!voidReached && sandPos.y == voidY) {
                        voidReached = true
                        totalSandBeforeVoid = totalSandAtRest
                    }
                }
            }
        }
    }

    override fun part1(): String {
        return totalSandBeforeVoid.toString()
    }

    override fun part2(): String {
        return totalSandAtRest.toString()
    }
}

fun main() {
    Day14().main()
}