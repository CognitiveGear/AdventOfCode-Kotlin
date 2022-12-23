@Suppress("unused")
enum class Dir(val delta : Point) {
    RIGHT(Point(1, 0)),
    DOWN(Point(0, -1)),
    LEFT(Point(-1, 0)),
    UP(Point(0, 1)),
    UP_LEFT(Point(-1, 1)),
    UP_RIGHT(Point(1, 1)),
    DOWN_LEFT(Point(-1, -1)),
    DOWN_RIGHT(Point(1, -1))
}

fun Dir.rotateLeft() : Dir {
    return when (this) {
        Dir.UP -> Dir.LEFT
        Dir.DOWN -> Dir.RIGHT
        Dir.LEFT -> Dir.DOWN
        Dir.RIGHT -> Dir.UP
        Dir.UP_LEFT -> Dir.DOWN_LEFT
        Dir.UP_RIGHT -> Dir.UP_LEFT
        Dir.DOWN_LEFT -> Dir.DOWN_RIGHT
        Dir.DOWN_RIGHT -> Dir.UP_RIGHT
    }
}

fun Dir.rotateRight() : Dir {
    return when (this) {
        Dir.UP -> Dir.RIGHT
        Dir.DOWN -> Dir.LEFT
        Dir.LEFT -> Dir.UP
        Dir.RIGHT -> Dir.DOWN
        Dir.UP_LEFT -> Dir.UP_RIGHT
        Dir.UP_RIGHT -> Dir.DOWN_RIGHT
        Dir.DOWN_LEFT -> Dir.UP_RIGHT
        Dir.DOWN_RIGHT -> Dir.DOWN_LEFT
    }
}
