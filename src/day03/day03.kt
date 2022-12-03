@file:Suppress("unused")
package day03

import readInput

fun priority(c: Char) : Int {
    return if (c.isLowerCase()) {
        c - 'a' + 1
    } else {
        c - 'A' + 27
    }
}

fun part1(file: List<String>) : String =
    file.sumOf {
        val numItems = it.length / 2
        priority(it.take(numItems).toSet().intersect(it.takeLast(numItems).toSet()).first())
    }.toString()

fun part2(file: List<String>) : String =
    file.chunked(3).sumOf { group ->
        priority(group.map { it.toSet() }.reduce { a, b -> a.intersect(b) }.first())
    }.toString()

fun main() {
    val dataFile = readInput("data")
    println(part1(dataFile))
    println(part2(dataFile))
}