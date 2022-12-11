class Day11 : AdventDay( 2022, 11) {

    private val initialItems : List<List<Long>>
    private val divisors : List<Long>
    private val throwToTrue : List<Int>
    private val throwToFalse : List<Int>
    private var worryDivisor : Boolean = true
    private val operations : List<((Long) -> Long)>
    private val inspects : MutableList<Long>
    private val lowestCommonMultiple : Long

    private fun List<MutableList<Long>>.round() {
        forEachIndexed { index, items ->
            items.forEach {
                inspects[index]++
                val newWorry =
                    if (worryDivisor) {
                        operations[index](it) / 3L
                    } else {
                        operations[index](it) % lowestCommonMultiple
                    }
                if ((newWorry % divisors[index]) == 0L) {
                    this[throwToTrue[index]].add(newWorry)
                } else {
                    this[throwToFalse[index]].add(newWorry)
                }
            }
            items.clear()
        }
    }

    init {
        val lines = input.lines().filter { it != "" }.chunked(6)
        initialItems = lines.map { monkey -> monkey[1].grabInts().map { it.toLong() } }
        val numOfMonkeys = initialItems.size
        inspects = MutableList(numOfMonkeys) { 0L }
        divisors = lines.map { monkey -> monkey[3].grabInts().first().toLong() }
        lowestCommonMultiple = divisors.fold(1L) { acc, l -> acc * l }
        throwToTrue = lines.map { monkey -> monkey[4].grabInts().first() }
        throwToFalse = lines.map { monkey -> monkey[5].grabInts().first() }
        operations = lines.map { monkey ->
            val opNumber = monkey[2].grabInts().firstOrNull()?.toLong()
            if (opNumber == null) {
                if ('+' in monkey[2]) {
                    { it: Long -> it + it }
                } else {
                    { it: Long -> it * it }
                }
            } else {
                if ('+' in monkey[2]) {
                    { it: Long -> it + opNumber }
                } else {
                    { it: Long -> it * opNumber }
                }
            }
        }
    }

    private fun List<Long>.monkeyBusiness() : String {
        val bigTwo = sortedDescending().take(2)
        return (bigTwo[0] * bigTwo[1]).toString()
    }

    override fun part1(): String {
        worryDivisor = true
        val currentItems = initialItems.map { it.toMutableList() }
        repeat(20) {
            currentItems.round()
        }
        return inspects.monkeyBusiness()
    }

    override fun part2(): String {
        worryDivisor = false
        inspects.replaceAll { 0L }
        val currentItems = initialItems.map { it.toMutableList() }
        repeat(10000) {
            currentItems.round()
        }
        return inspects.monkeyBusiness()
    }
}

fun main() {
    Day11().main()
}