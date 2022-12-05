@file:Suppress("unused")

import kotlinx.coroutines.runBlocking
import java.io.File

abstract class AdventDay(val input: List<String>) {

    constructor(year: Int, day: Int) : this(
        runBlocking {
            checkOrGetInput(year, day, File("data"))
        }
    )
    constructor(fileName: String) : this (
        inputList(fileName)
    )

    abstract fun part1() : String
    abstract fun part2() : String
    fun run() {
        println(part1())
        println(part2())
    }
}