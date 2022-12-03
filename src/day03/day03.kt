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

fun part1(file: List<String>) : String {
    return file.sumOf {
        val numItems = it.length / 2
        val comp1 = it.take(numItems).toSet()
        val comp2 = it.takeLast(numItems).toSet()
        priority (comp1.intersect(comp2).first())
    }.toString()
}

fun part2(file: List<String>) : String {
    val groups = file.chunked(3)
    return groups.sumOf {  group ->
        val sets = group.map { it.toSet() }
        priority(sets[0].intersect(sets[1].intersect(sets[2])).first())
    }.toString()
}

fun main() {
    val dataFile = readInput("data")
    println(part1(dataFile))
    println(part2(dataFile))
}