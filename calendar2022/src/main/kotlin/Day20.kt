class Day20 : AdventDay(2022, 20) {

    class Code(val originalIndex: Int, val value: Long) {
        operator fun times(arg: Long) : Code = Code(originalIndex, value * arg)
        override fun toString() : String = "($originalIndex, $value)"
    }

    val cipher = lines.mapIndexed { index, line ->
        Code(index, line.grabInts().first().toLong())
    }

    fun List<Code>.mixStep(index: Int) : List<Code> {
        val code = get(index)
        val endIndex = if (code.value >= 0 ) {
            Math.floorMod(index + code.value, size - 1)
        } else {
            Math.floorMod(index + code.value - 1, size - 1) + 1
        }
        return when {
            endIndex == index -> this
            endIndex < index -> {
                val listBefore = if (endIndex == 0) {
                    listOf()
                } else {
                    subList(0, endIndex)
                }
                val listBetween = subList(endIndex, index)
                val listAfter = if (index + 1 == size) {
                    listOf()
                } else {
                    subList(index + 1, size)
                }
                listBefore + code + listBetween + listAfter
            }
            else -> {
                val listBefore = if (index == 0) {
                    listOf()
                } else {
                    subList(0, index)
                }
                val listBetween = subList(index + 1, endIndex + 1)
                val listAfter = if (endIndex + 1 == size) {
                    listOf()
                } else {
                    subList(endIndex + 1, size).toList()
                }
                listBefore + listBetween + code + listAfter
            }
        }
    }

    fun List<Code>.getWrap(index: Int) : Long {
        return get(Math.floorMod(index,  size)).value
    }

    fun List<Code>.mix(numberOfTimes : Int) : List<Code> {
        var current = this
        repeat(numberOfTimes) {
            for (index in indices) {
                val codeIndex = current.indexOfFirst { it.originalIndex == index }
                current = current.mixStep(codeIndex)
            }
        }
        return current
    }

    fun List<Code>.groveCoords() : Long {
        val zeroIndex = indexOfFirst{ it.value == 0L }
        return getWrap(zeroIndex + 1000) + getWrap(zeroIndex + 2000) + getWrap(zeroIndex + 3000)
    }

    override fun part1(): String {
        val new = cipher.mix(1)
        return new.groveCoords().toString()
    }

    override fun part2(): String {
        val new = cipher.map { it * 811589153L }.mix(10)
        return new.groveCoords().toString()
    }
}

fun main() {
    Day20().main()
}