package day02

import readInput

enum class PlayerChoice {
    X,
    Y,
    Z
}

enum class OpponentChoice {
    A,
    B,
    C
}

typealias Game = Pair<OpponentChoice, PlayerChoice>

fun pointsPerGame(game: Game) : Int {
    return when (game.first) {
        OpponentChoice.A -> {
            when (game.second) {
                PlayerChoice.X -> 1 + 3
                PlayerChoice.Y -> 2 + 6
                PlayerChoice.Z -> 3 + 0
            }
        }
        OpponentChoice.B -> {
            when (game.second) {
                PlayerChoice.X -> 1 + 0
                PlayerChoice.Y -> 2 + 3
                PlayerChoice.Z -> 3 + 6
            }
        }
        OpponentChoice.C -> {
            when (game.second) {
                PlayerChoice.X -> 1 + 6
                PlayerChoice.Y -> 2 + 0
                PlayerChoice.Z -> 3 + 3
            }
        }
    }
}

fun pointsPerGame2(game: Game) : Int {
    var result = 0
    result += when (game.second) {
        PlayerChoice.X -> {
           0 + when (game.first) {
               OpponentChoice.A -> 3
               OpponentChoice.B -> 1
               OpponentChoice.C -> 2
           }
        }
        PlayerChoice.Y -> {
            3 + when (game.first) {
                OpponentChoice.A -> 1
                OpponentChoice.B -> 2
                OpponentChoice.C -> 3
            }
        }
        PlayerChoice.Z -> {
            6 + when (game.first) {
                OpponentChoice.A -> 2
                OpponentChoice.B -> 3
                OpponentChoice.C -> 1
            }
        }
    }
    return result
}

fun main() {
    val input = readInput("data")
    println(part1(input))
    println(part2(input))
}

fun toGames(input: List<String>): List<Game> {
    return input.map {
        Pair(OpponentChoice.values()[it[0] - 'A'], PlayerChoice.values()[it[2] - 'X'])
    }
}

fun part1(input: List<String>) : Int {
    val games = toGames(input)
    return games.sumOf {
        pointsPerGame(it)
    }
}

fun part2(input: List<String>) : Int {
    val games = toGames(input)
    return games.sumOf {
        pointsPerGame2(it)
    }
}