package day01

import readInput

fun calorieList(input: List<String>): List<Int> {
    return input
        .joinToString("\n")
        .split("\n\n")
        .map { subString -> subString.split('\n')
            .map {
                it.toInt()
            }
        }
        .map { it.sum() }
}

fun part1(list: List<Int>): Int {
    val maxValue = list.max()
    return maxValue
}

fun part2(list: List<Int>): Int {
    return list.sorted().takeLast(3).sum()
}

fun main() {
    val dataInput = readInput("data")
    val calorieList = calorieList(dataInput)
    println(part1(calorieList))
    println(part2(calorieList))
}
