import java.lang.Integer.max
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09 : AdventDay(2022, 9) {

    data class Knot(var x: Int, var y: Int) {
        var moveCallback : ((Pair<Int, Int>) -> Unit)? = null
        fun moveBy(diffX : Int, diffY: Int) {
            x += diffX
            y += diffY
            moveCallback?.invoke(x to y)
        }
    }

    private val allTail1Positions : MutableSet<Pair<Int, Int>> = mutableSetOf((0 to 0))
    private val allTail9Positions : MutableSet<Pair<Int, Int>> = mutableSetOf((0 to 0))

    private val knots : List<Knot> = List(10) {
        Knot(0, 0)
    }.apply {
        get(1).moveCallback = { allTail1Positions.add(it) }
        get(9).moveCallback = { allTail9Positions.add(it) }
    }

    private fun moveTail(head : Knot, tail : Knot) {
        val lenX = (head.x - tail.x)
        val lenY = (head.y - tail.y)
        if (max(lenX.absoluteValue, lenY.absoluteValue) > 1) {
            tail.moveBy(lenX.sign, lenY.sign)
        }
    }

    init {
        val lines = input.lines()
        val ropeCallOrder = knots.windowed(2, 1)
        fun List<List<Knot>>.timeEvolution(diffX: Int, diffY: Int, number: Int) {
            repeat(number) {
                knots[0].moveBy(diffX, diffY)
                forEach { (head, tail) -> moveTail(head, tail) }
            }
        }
        lines.forEach {
            val number = it.grabInts().first()
            when (it.take(1)) {
                "U" -> ropeCallOrder.timeEvolution(0, 1, number)
                "D" -> ropeCallOrder.timeEvolution(0, -1, number)
                "L" -> ropeCallOrder.timeEvolution(-1, 0, number)
                "R" -> ropeCallOrder.timeEvolution(1, 0, number)
            }
        }
    }

    override fun part1(): String {
        return allTail1Positions.size.toString()
    }

    override fun part2(): String {
        return allTail9Positions.size.toString()
    }
}

fun main() {
    Day09().main()
}