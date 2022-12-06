import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.min

class Day05 : AdventDay(2022, 5) {

    private val craneState1 : List<Stack<Char>> = List(9) { Stack() }

    private val craneState2 : List<ArrayDeque<Char>> = List(9) { ArrayDeque() }

    data class Instruction(val s: Int, val n: Int, val e: Int)

    private fun List<Stack<Char>>.doInstruction1(int: Instruction) {
        repeat(int.n) {
            if (this[int.s].isNotEmpty()) {
                this[int.e].push(this[int.s].pop())
            }
        }
    }

    private fun List<ArrayDeque<Char>>.doInstruction2(int: Instruction) {
        val number = min(int.n, this[int.s].size)
        val bit = this[int.s].takeLast(number)
        repeat (number) {
            this[int.s].removeLast()
        }
        this[int.e].addAll(bit)
    }

    private fun String.toInstruction() : Instruction {
        val (a, b, c) = this.grabInts()
        return Instruction(b - 1, a, c - 1)
    }

    private fun processInput(){
        val (crane, instructions) = input.split("\n\n")
        // Deal with the crane state
        crane.split('\n').reversed().drop(1).forEach { line ->
            line.slice(1..line.length step 4).withIndex().filter { it.value.isLetter() }.forEach {
                craneState1[it.index].push(it.value)
                craneState2[it.index].add(it.value)
            }
        }
        // Deal with the instructions
        instructions.split('\n').map { it.toInstruction() }.forEach {
            craneState1.doInstruction1(it)
            craneState2.doInstruction2(it)
        }
    }

    init {
        processInput()
    }

    override fun part1(): String {
        var result = ""
        craneState1.forEach {
            if (it.isNotEmpty()) {
                result += it.peek().toString()
            }
        }
        return result
    }

    override fun part2(): String {
        var result = ""
        craneState2.forEach {
            if (it.isNotEmpty()) {
                result += it.last()
            }
        }
        return result
    }
}

fun main() {
    Day05().main()
}