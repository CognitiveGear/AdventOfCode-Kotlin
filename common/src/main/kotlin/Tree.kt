@file:Suppress("unused")

import java.util.*

sealed class Node<T>
/**
 * Custom Tree implementation. Assumes that it has *exclusive* access to children.
 */
class Tree<T>(val children: MutableList<Node<T>> = mutableListOf())
    : Node<T>(), MutableList<Node<T>> by children
{

    operator fun plus(node: Node<T>) : Tree<T> {
        children.add(node)
        return this
    }

    fun treeParse(action: (T) -> Unit) {
        val iteratorStack: Stack<MutableListIterator<Node<T>>> = Stack()
        iteratorStack.push(children.listIterator())
        var currentIterator : MutableListIterator<Node<T>>
        while (iteratorStack.isNotEmpty()) {
            currentIterator = iteratorStack.pop()
            while (currentIterator.hasNext()) {
                when (val value = currentIterator.next()) {
                    is Leaf -> action(value.value)
                    is Tree -> {
                        iteratorStack.push(currentIterator)
                        currentIterator = value.children.listIterator()
                    }
                }
            }
        }
    }

    fun treeParseIndexed(action: (List<Int>, T) -> Unit) {
        val iteratorStack = Stack<MutableListIterator<Node<T>>>()
        iteratorStack.push(children.listIterator())
        val address = Stack<Int>()
        var currentIterator : MutableListIterator<Node<T>>
        while (iteratorStack.isNotEmpty()) {
            currentIterator = iteratorStack.pop()
            while (currentIterator.hasNext()) {
                val index = currentIterator.nextIndex()
                when (val value = currentIterator.next()) {
                    is Leaf -> action(address + index, value.value)
                    is Tree -> {
                        iteratorStack.push(currentIterator)
                        currentIterator = value.children.listIterator()
                        address.push(index)
                    }
                }
            }
            address.pop()
        }
    }

    override fun toString(): String = children.joinToString(",", "[", "]") { it.toString() }
}

class Leaf<T>(var value: T) : Node<T>() {
    override fun toString() : String = value.toString()
}