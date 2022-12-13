class Day13 : AdventDay(2022, 13) {

    sealed class Node(var parent : Branch?) {
        operator fun compareTo(other : Node) : Int =
            when {
                this is Branch && other is Branch -> this.compareTo(other)
                this is Branch && other is Leaf -> this.compareTo(other)
                this is Leaf && other is Branch -> -1 * other.compareTo(this)
                this is Leaf && other is Leaf -> this.compareTo(other)
                else -> {
                    throw IllegalStateException("Shouldn't be able to get here...")
                }
            }
    }
    class Leaf(private val data: Int, parent: Branch? = null) : Node(parent) {
        operator fun compareTo(other: Leaf) : Int = data.compareTo(other.data)
        fun expand() : Branch = Branch(this.parent) + Leaf(data)
    }
    class Branch(parent: Branch? = null, private val children : MutableList<Node> = mutableListOf()) : Node(parent) {
        operator fun plus(other: Node) : Branch {
            other.parent = this
            children.add(other)
            return this
        }
        operator fun compareTo(right: Branch) : Int {
            children.zip(right.children).forEach {
                val comparison = (it.first).compareTo(it.second)
                if (comparison != 0) {
                    return comparison
                }
            }
            return children.size.compareTo(right.children.size)
        }
        operator fun compareTo(other: Leaf) : Int = compareTo(other.expand())
    }


    private fun String.treeParse() : Branch {
        val regex  = """\[|]|-?\d+""".toRegex()
        val matches = regex.findAll(this)
        val root = Branch()
        var currentNode : Branch = root
        for (match in matches) {
            currentNode = when (match.value) {
                "[" -> {
                    Branch().also {
                        currentNode + it
                    }
                }
                "]" -> currentNode.parent!!
                else -> currentNode + Leaf(match.value.toInt())
            }
        }
        return root
    }

    private val packets : List<Branch> = lines.filter { it != ""}.map { it.treeParse() }

    override fun part1(): String {
        return packets.chunked(2).withIndex().filter {
            it.value[0] < it.value[1]
        }.sumOf {
            it.index + 1
        }.toString()
    }

    override fun part2(): String {
        val divider1 = Branch() + (Branch() + Leaf(2))
        val divider2 = Branch() + (Branch() + Leaf(6))
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