@Suppress("unused")
enum class Dir(val delta : Point) {
    UP(Point(0, 1)),
    DOWN(Point(0, -1)),
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
    UP_LEFT(Point(-1, 1)),
    UP_RIGHT(Point(1, 1)),
    DOWN_LEFT(Point(-1, -1)),
    DOWN_RIGHT(Point(1, -1))
}