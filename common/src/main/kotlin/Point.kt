@file:Suppress("unused")

import kotlin.math.absoluteValue
import kotlin.math.max

class Point(val x: Int, val y: Int) : Collection<Int> {
    operator fun plus(arg: Int) = Point(x + arg, y + arg)
    operator fun plus(arg: Point) = Point(x + arg.x, y + arg.y)
    operator fun minus(arg: Int) = Point(x - arg, y - arg)
    operator fun minus(arg: Point) = Point(x - arg.x, y - arg.y)
    operator fun times(arg: Int) = Point(x * arg, y * arg)
    operator fun times(arg: Point) = Point(x * arg.x, y * arg.y)
    operator fun div(arg: Int) = Point(x / arg, y / arg)
    operator fun div(arg: Point) = Point(x / arg.x, y / arg.y)
    /**
     * Also called the taxicab distance, or Manhattan distance. Gives the distance between two points
     * while only travelling across adjacent squares (excluding diagonals).
     */
    infix fun l1Norm(arg: Point) = (x - arg.x).absoluteValue + (y - arg.y).absoluteValue
    fun l1Neighbors() : Sequence<Point> =
        sequence {
            yield(Point(x - 1, y))
            yield(Point(x + 1, y))
            yield(Point(x, y - 1))
            yield(Point(x, y + 1))
        }

    /**
     * L-Inf norm, also called the Chebyshev distance. Gives the distance between two points when traveling
     * across all touching squares (including diagonals).
     */
    infix fun lInfNorm(arg: Point) = max((x - arg.x).absoluteValue, (y - arg.y).absoluteValue)

    fun lInfNeighbors() : Sequence<Point> =
        sequence {
            yield(Point(x - 1, y - 1))
            yield(Point(x - 1, y))
            yield(Point(x - 1, y + 1))
            yield(Point(x, y - 1))
            yield(Point(x, y + 1))
            yield(Point(x + 1, y - 1))
            yield(Point(x + 1, y))
            yield(Point(x + 1, y + 1))
        }
    /**
     * Will return Greater if this value has any coordinate larger than the argument, but only equal when they
     * are exactly equal.
     */
    operator fun compareTo(arg: Point) : Int =
        if (x > arg.x) {
            1
        } else if (y > arg.y) {
            1
        } else if (x == arg.x && y == arg.y) {
            0
        } else {
            -1
        }
    operator fun component1() = x
    operator fun component2() = y
    override val size: Int = 2
    override fun isEmpty() = false
    override fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            private var notEnd : Boolean = true
            override fun hasNext(): Boolean = notEnd
            override fun next(): Int =
                if (notEnd) {
                    notEnd = false
                    x
                } else {
                    y
                }
        }
    }
    override fun contains(element: Int): Boolean = element == x || element == y
    override fun containsAll(elements: Collection<Int>): Boolean = elements.all { it in this }
    override fun hashCode() : Int {
        return if (x > y) {
            y * y + x
        } else {
            x * x + x + y
        }
    }
    override fun toString() = "($x, $y)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Point
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }
}