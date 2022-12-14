import java.util.*

class Day13 : AdventDay(2022, 13) {

    operator fun Node<Int>.compareTo(other : Node<Int>) : Int =
        when {
            this is Tree<Int> && other is Tree<Int> -> this.compareTo(other)
            this is Tree<Int> && other is Leaf<Int> -> this.compareTo(other)
            this is Leaf<Int> && other is Tree<Int> -> -1 * other.compareTo(this)
            this is Leaf<Int> && other is Leaf<Int> -> this.compareTo(other)
            else -> throw IllegalStateException("Shouldn't be able to get here...")
        }

    operator fun Leaf<Int>.compareTo(other: Leaf<Int>) : Int = value.compareTo(other.value)
    fun Leaf<Int>.expand() : Tree<Int> = Tree<Int>() + this

    operator fun Tree<Int>.compareTo(other: Leaf<Int>) : Int = compareTo(other.expand())

    operator fun Tree<Int>.compareTo(other: Tree<Int>) : Int {
        children.zip(other.children).forEach {
            val comparison = (it.first).compareTo(it.second)
            if (comparison != 0) {
                return comparison
            }
        }
        return children.size.compareTo(other.children.size)
    }

    private fun String.treeParse() : Tree<Int> {
        val regex  = """\[|]|-?\d+""".toRegex()
        val matches = regex.findAll(this)
        val tree : Stack<Tree<Int>> = Stack()
        for (match in matches) {
            when (match.value) {
                "[" -> {
                    val newTree = Tree<Int>()
                    if (tree.isNotEmpty()) {
                        tree.peek().add(newTree)
                    }
                    tree.push(newTree)
                }
                "]" -> {
                    val newTree = tree.pop()
                    if (tree.isEmpty()) {
                        return newTree
                    }
                }
                else -> tree.peek().add(Leaf(match.value.toInt()))
            }
        }
        return tree.peek()
    }

    private val packets : List<Tree<Int>> = lines.filter { it != "" }.map { it.treeParse() }

    init {
        packets.forEach {
            println(it)
        }
    }

    override fun part1(): String {
        return packets.chunked(2).withIndex().filter {
            it.value[0] < it.value[1]
        }.sumOf {
            println("Pair ${it.index + 1}")
            it.index + 1
        }.toString()
    }

    override fun part2(): String {
        val divider1 = Tree<Int>() + (Tree<Int>() + Leaf(2))
        val divider2 = Tree<Int>() + (Tree<Int>() + Leaf(6))
        val sortedPackets = (packets + listOf(divider1, divider2))
            .toMutableList()
            .sortedWith { o1, o2 ->
                o1!!.compareTo(
                    o2!!
                )
            }
        val index1 = sortedPackets.indexOf(divider1) + 1
        val index2 = sortedPackets.indexOf(divider2) + 1
        return (index1 * index2).toString()
    }
}

fun main() {
    Day13().main()
}