@file:Suppress("unused")
package day03

import readInput

fun priority(c: Char) : Int {
    if (c.isLowerCase()) {
        return c - 'a' + 1
    } else {
        return c - 'A' + 27
    }
}

fun part1(file: List<String>) : String {
    var sum = 0
    for (sack in file) {
        val numItems = sack.length / 2
        val comp1 = sack.take(numItems)
        val comp2 = sack.takeLast(numItems)
        val set1 = comp1.toSortedSet()
        val set2 = comp2.toSortedSet()
        val badItem = set1.intersect(set2).first()
        sum += priority(badItem)
    }
    return sum.toString()
}

fun part2(file: List<String>) : String {
    var sum = 0
    val fileMut = file.toMutableList()
    while (fileMut.size > 0) {
        val group = fileMut.take(3).map {
            it.toSortedSet()
        }
        repeat(3) {
            fileMut.removeAt(0)
        }
        val badge = group[0].intersect(group[1].intersect(group[2])).first()
        sum += priority(badge)
    }
    return sum.toString()
}

fun main() {
    val dataFile = readInput("data")
    println(part1(dataFile))
    println(part2(dataFile))
}