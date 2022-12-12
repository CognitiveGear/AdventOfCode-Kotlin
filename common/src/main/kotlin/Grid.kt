
import java.lang.IllegalArgumentException

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Grid<T>(val data : List<MutableList<T>>) {
    val maxRow : Int = data.size
    val maxCol : Int = data.first().size
    constructor(rows: Int, cols: Int, init: (Int, Int) -> T) : this(
        List(cols) { row ->
            MutableList(rows) { col ->
                init(row, col)
            }
        })

    init {
        data.drop(1).forEach {
            if (it.size != maxCol) {
                throw IllegalArgumentException("Input data matrix has mismatched rows with different lengths")
            }
        }
    }
    operator fun get(row: Int, col: Int) : T = data[row][col]
    operator fun get(pos: Point) : T = data[pos.y][pos.x]
    operator fun set(row: Int, col: Int, value: T) { data[row][col] = value }
    operator fun set(pos: Point, value: T) { data[pos.y][pos.x] = value}
    operator fun contains(t: T) : Boolean {
        for (row in data) {
            for (value in row) {
                if (value == t) {
                    return true
                }
            }
        }
        return false
    }
    fun containsIndex(p: Point) : Boolean = p.x >= 0 && p.y >= 0 && p.x < maxCol && p.y < maxRow
    fun forEachIndexed(action: (Int, Int, T) -> Unit) {
        for (row in 0 until maxRow) {
            for (col in 0 until maxCol) {
                action(row, col, data[row][col])
            }
        }
    }
    fun getPosOf(t : T) : Set<Point> {
        val set = mutableSetOf<Point>()
        for (row in 0 until maxRow) {
            for (col in 0 until maxCol) {
                if (data[row][col] == t) {
                    set.add(Point(col, row))
                }
            }
        }
        return set
    }
    fun l1Neighbors(p: Point) : Sequence<Point> {
        return sequenceOf(
            Point(p.x - 1, p.y),
            Point(p.x + 1, p.y),
            Point(p.x, p.y + 1),
            Point(p.x, p.y - 1)
        ).filter(this::containsIndex)
    }
    fun lInfNeighbors(p: Point) : Sequence<Point> {
        return sequenceOf(
            Point(p.x - 1, p.y - 1),
            Point(p.x - 1, p.y),
            Point(p.x - 1, p.y + 1),
            Point(p.x, p.y - 1),
            Point(p.x, p.y),
            Point(p.x, p.y + 1),
            Point(p.x + 1, p.y - 1),
            Point(p.x + 1, p.y),
            Point(p.x + 1, p.y + 1)
        ).filter(this::containsIndex)
    }
    override fun toString(): String = data.joinToString("\n") { it.toString() }
}