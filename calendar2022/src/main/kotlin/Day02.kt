class Day02 : AdventDay(2, 2022) {

    enum class Player { X, Y, Z }
    enum class Opponent { A, B, C }
    data class Game(val o: Opponent, val p : Player)

    private fun pointsPerGame(game: Game): Int =
        when (game.o) {
            Opponent.A -> {
                when (game.p) {
                    Player.X -> 1 + 3
                    Player.Y -> 2 + 6
                    Player.Z -> 3 + 0
                }
            }
            Opponent.B -> {
                when (game.p) {
                    Player.X -> 1 + 0
                    Player.Y -> 2 + 3
                    Player.Z -> 3 + 6
                }
            }
            Opponent.C -> {
                when (game.p) {
                    Player.X -> 1 + 6
                    Player.Y -> 2 + 0
                    Player.Z -> 3 + 3
                }
            }
        }

    private fun pointsPerGame2(game: Game): Int =
        when (game.p) {
            Player.X -> {
                0 + when (game.o) {
                    Opponent.A -> 3
                    Opponent.B -> 1
                    Opponent.C -> 2
                }
            }
            Player.Y -> {
                3 + when (game.o) {
                    Opponent.A -> 1
                    Opponent.B -> 2
                    Opponent.C -> 3
                }
            }
            Player.Z -> {
                6 + when (game.o) {
                    Opponent.A -> 2
                    Opponent.B -> 3
                    Opponent.C -> 1
                }
            }
        }

    private fun String.toGame(): Game = Game(Opponent.values()[this[0] - 'A'], Player.values()[this[2] - 'X'])

    private val games = input.split('\n').map { it.toGame() }

    override fun part1(): String =
        games.sumOf {
            pointsPerGame(it)
        }.toString()

    override fun part2(): String =
        games.sumOf {
            pointsPerGame2(it)
        }.toString()
}

fun main() {
    val day = Day02()
    day.part1()
    day.part2()
}
