@Suppress("unused")
enum class Dir(val delta : Point) {
    E(Point(1, 0)),
    S(Point(0, -1)),
    W(Point(-1, 0)),
    N(Point(0, 1)),
    NW(Point(-1, 1)),
    NE(Point(1, 1)),
    SW(Point(-1, -1)),
    SE(Point(1, -1))
}

fun Dir.rotateLeft() : Dir {
    return when (this) {
        Dir.N -> Dir.W
        Dir.S -> Dir.E
        Dir.W -> Dir.S
        Dir.E -> Dir.N
        Dir.NW -> Dir.SW
        Dir.NE -> Dir.NW
        Dir.SW -> Dir.SE
        Dir.SE -> Dir.NE
    }
}

fun Dir.rotateRight() : Dir {
    return when (this) {
        Dir.N -> Dir.E
        Dir.S -> Dir.W
        Dir.W -> Dir.N
        Dir.E -> Dir.S
        Dir.NW -> Dir.NE
        Dir.NE -> Dir.SE
        Dir.SW -> Dir.NE
        Dir.SE -> Dir.SW
    }
}
