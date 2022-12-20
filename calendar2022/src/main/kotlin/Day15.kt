import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

class Day15 : AdventDay(2022, 15) {

    val beacons : List<Point>
    val sensors : List<Point>
    val radii : Map<Point, Int>
    val targetY : Int = 2000000

    init {
        val numbers: List<Pair<Point, Point>> =
            lines.map { line ->
                line.grabInts().chunked(2).map { numbers ->
                    Point(numbers.first(), numbers.last())
                }
            }.map {
                it.first() to it.last()
            }
        sensors = numbers.map { it.first }
        beacons = numbers.map { it.second }
        radii = numbers.associate { it.first to (it.first l1 it.second) }
    }

    private fun IntRange.overlaps(other: IntRange) : Boolean =
        other.first in this || other.last in this || first in other || last in other

    operator fun IntRange.contains(other: IntRange) : Boolean =
        (other.first in this && other.last in this)

    private fun MutableSet<IntRange>.normalize(other: IntRange) {
        var insertion = other
        var overlap : IntRange?
        do {
            overlap = firstOrNull {
                it.overlaps(insertion)
            }?.also {
                remove(it)
                insertion = insertion.combineOrNull(it)!!
            }
        } while (overlap != null)
        add(insertion)
    }

    private fun IntRange.combineOrNull(other:  IntRange) : IntRange? =
        when {
            other in this -> this
            this in other -> other
            this.overlaps(other) -> {
                if (first in other) {
                    other.first..last
                } else {
                    first..other.last
                }
            }
            else -> null
        }

    override fun part1(): String {
        val xRanges = sensors
        .map { sensor ->
            val reducedDistance = radii[sensor]!! - (sensor.y - targetY).absoluteValue
            sensor.x to reducedDistance
        }.filter {
            it.second >= 0
        }.map {
            (it.first - it.second)..(it.first + it.second)
        }
        val allRanges : MutableSet<IntRange> = mutableSetOf()
        xRanges.forEach {
            allRanges.normalize(it)
        }
        var totalRangeSize = allRanges.sumOf {
            it.last - it.first + 1
        }
        allRanges.forEach { range ->
            beacons.distinct().forEach {
                if (it.y == targetY && it.x in range) {
                    totalRangeSize--
                }
            }
            sensors.distinct().forEach {
                if (it.y == targetY && it.x in range) {
                    totalRangeSize--
                }
            }
        }
        return totalRangeSize.toString()
    }

    fun Point.tuning() : Long = x.toLong() * 4000000L + y.toLong()

    data class Line(
        val left : Point,
        val right : Point
    ) {
        val m : Double?
        val c : Double?

        init {
            val y1 = left.y.toDouble()
            val y2 = right.y.toDouble()
            val x1 = left.x.toDouble()
            val x2 = right.x.toDouble()
            if (x2 - x1 == 0.0) {
                m = null
                c = null
            } else {
                m = (y2 - y1) / (x2 - x1)
                c = y2 - m * x2
            }
        }

        fun intersect(arg: Line) : Point? {
            if (m == null || c == null || arg.m == null || arg.c == null) {
                return null
            } else {
                if (m - arg.m == 0.0) {
                    return null
                }
                val x = ((arg.c - c) / (m - arg.m))
                val y = (m * x + c)
                val maxX = max(left.x, right.x)
                val minX = min(left.x, right.x)
                if (x > maxX || x < minX) {
                    return null
                }
                return Point(x.roundToInt(), y.roundToInt())
            }
        }
    }

    override fun part2(): String {
        val opposingPairs : List<Pair<Point, Point>> =
            sensors.asSequence().flatMap { left ->
                sensors.asSequence().filter { right ->
                    left != right &&
                    (left l1 right) == radii[left]!! + radii[right]!!
                }.map { right ->
                    if (left.x < right.x) {
                        left to right
                    } else {
                        right to left
                    }
                }
            }.toList()
        fun List<Pair<Point, Point>>.toLineList() : List<Line> {
            return map {
                val (p, other) =
                    if (radii[it.first]!! < radii[it.second]!!) {
                        it
                    } else {
                        it.second to it.first
                    }
                Line(
                    p + Point(0, radii[p]!! + 1),
                    p + Point((radii[p]!! + 1) * (other.y - p.y).sign, 0)
                )
            }
        }
        val leanLeft = opposingPairs.filter {
            it.first.y > it.second.y
        }.toLineList()
        val leanRight = opposingPairs.filter {
            it.first.y < it.second.y
        }.toLineList()
        val result = leanLeft.asSequence().flatMap { left ->
            leanRight.asSequence().mapNotNull { right ->
                left.intersect(right)
            }
        }.filterNot {
            it.x < 0 || it. y < 0 || it.x > 4000000 || it.y > 4000000 ||
            sensors.any { sensor ->
                it l1 sensor <= radii.getValue(sensor)
            }
        }.first().tuning().toString()
        return result
    }
}

fun main() {
    Day15().main()
}