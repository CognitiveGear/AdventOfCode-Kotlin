@file:Suppress("MemberVisibilityCanBePrivate")

import kotlinx.coroutines.*
import java.lang.Integer.max
import java.util.*

class Day17 : AdventDay(2022, 17) {

    class TetrisGame(playHeight: Int) {
        val width = 7
        var rocks = 0
        val grid = Grid(playHeight + 1, width, ' ')
        var topFilled = -1
        val heightList = mutableListOf(0)

        fun hAt(num : Int) : Long = (heightList[num]).toLong()
    }

    sealed class Tetronimo(val game: TetrisGame) {
        abstract val space : Set<Point>
        var pos : Point = Point(2, 0)

        fun spawn() {
            pos = Point(2, game.topFilled + 4)
        }

        fun canFit(dir: Dir) : Boolean {
            return space.all {
                val newPos = pos + it + dir
                (newPos) in game.grid && game.grid[newPos] != '#'
            }
        }

        fun move(dir: Dir) {
            pos += dir
        }

        fun draw() {
            val filled = space.map { it + pos }
            filled.forEach { game.grid[it] = '#' }
            game.topFilled = max(game.topFilled, filled.maxOf { it.y })
            game.rocks++
            game.heightList.add(game.topFilled + 1)
        }
    }

    class Floor(game: TetrisGame) : Tetronimo(game) {
        override val space = setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))
    }

    class Cross(game: TetrisGame) : Tetronimo(game) {
        override val space = setOf(
            Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2)
        )
    }

    class L(game: TetrisGame) : Tetronimo(game) {
        override val space = setOf(
            Point(2, 0), Point(2, 1), Point(2, 2), Point(1, 0), Point(0, 0)
        )
    }
    class Pipe(game: TetrisGame) : Tetronimo(game) {
        override val space = setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))
    }
    class Square(game: TetrisGame) : Tetronimo(game) {
        override val space = setOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1))
    }

    fun moveSequence() : Sequence<Dir> = sequence {
        while(true) {
            for (move in moveList) {
                yield(move)
            }
            if (minimumPeriodLength == 0) {
                minimumPeriodLength = tetris.rocks
            }
        }
    }

    fun pieceSequence(game: TetrisGame) : Sequence<Tetronimo> = sequence {
        while(true) {
            yield(Floor(game))
            yield(Cross(game))
            yield(L(game))
            yield(Pipe(game))
            yield(Square(game))
        }
    }

    fun play(game: TetrisGame, maxRocks: Int) : Int {
        val pieceIterator = pieceSequence(game).iterator()
        val moveIterator = moveSequence().iterator()
        while (game.rocks < maxRocks) {
            val piece = pieceIterator.next()
            piece.spawn()
            while(true) {
                val move = moveIterator.next()
                if (piece.canFit(move)) {
                    piece.move(move)
                } else {
                    if (move == Dir.S) {
                        break
                    }
                }
            }
            piece.draw()
        }
        return game.heightList.last()
    }

    data class Period(val len: Int, val off: Int) {
        fun longer() = Period(len + 1, off)

        fun shifter() = Period(len, off + 1)
    }

    val periodGraph = InfiniteGraph<Period>(
        {
            sequence {
                val checkLen = 3 * it.len + it.off + 1
                if (checkLen < tetris.heightList.size) {
                    yield(it.shifter())
                }
                if (checkLen + 3 < tetris.heightList.size) {
                    yield(it.longer())
                }
            }
        }
    )

    val moveList = input.flatMap {
        when (it) {
            '>' -> listOf(Dir.E, Dir.S)
            '<' -> listOf(Dir.W, Dir.S)
            else -> throw IllegalStateException("Bad input")
        }
    }
    var minimumPeriodLength : Int = 0
    val maxPlayHeight = 100000
    val tetris = TetrisGame(maxPlayHeight)
    val part1Result : Int = play(tetris, 2022)
    val found : Period
    val dH : Long
    val dOffset : Long

    init {
        play(tetris, 50000)
        found = periodGraph.searchBy(
            Period(minimumPeriodLength, 5),
            compareBy {
                it.off + it.len
            }
        ) { period ->
            val offset1 = tetris.hAt(period.off - 1)
            val offset2 = tetris.hAt(period.off + period.len - 1)
            val offset3 = tetris.hAt(period.off + 2 * period.len - 1)
            val offset4 = tetris.hAt(period.off + 3 * period.len - 1)
            ((period.off) until (period.off + period.len)).all {
                val diff = tetris.hAt(it) - offset1
                diff == tetris.hAt(it + period.len) - offset2
                        && diff == tetris.hAt(it + 2 * period.len) - offset3
                        && diff == tetris.hAt(it + 3 * period.len) - offset4
            }
        }.end
        /*
        found = periodFinder()
         */
        dOffset = tetris.hAt(found.off)
        dH = tetris.hAt(found.off + found.len) - dOffset
    }

    override fun part1(): String {
        return part1Result.toString()
    }

    override fun part2(): String {
        val end = 1000000000000L
        val offset = found.off.toLong()
        val cycle = found.len.toLong()
        val tail = (end - offset) % cycle
        val dTail = tetris.hAt(found.off + tail.toInt()) - dOffset
        val periodContribution = ((end - offset - tail) / cycle) * dH
        return (dOffset + dTail + periodContribution).toString()
    }
}

fun main() {
    Day17().main()
}