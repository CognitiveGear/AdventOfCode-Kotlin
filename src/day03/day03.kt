@file:Suppress("unused")
package day03

import readInput

fun Char.priority() : Int {
    return if (isLowerCase()) {
        this - 'a' + 1
    } else {
        this - 'A' + 27
    }
}

fun List<String>.commonItemPriority() : Int =
    this.map { it.toSet() }.reduce { a, b -> a intersect b }.first().priority()

fun part1(file: List<String>) : String =
    file.sumOf { elf ->
        elf.chunked(elf.length / 2).commonItemPriority()
    }.toString()

fun part2(file: List<String>) : String =
    file.chunked(3).sumOf { group ->
        group.commonItemPriority()
    }.toString()

fun main() {
    val dataFile = readInput("data")
    println(part1(dataFile))
    println(part2(dataFile))
}