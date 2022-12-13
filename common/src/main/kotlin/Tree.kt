@file:Suppress("unused")
sealed class Node<T>(var parent: Node<T>?)

class Tree<T>(private val children: ArrayDeque<Node<T>>, parent: Node<T>? = null)
    : Node<T>(parent)
{
    operator fun plus(other: Node<T>) {
        other.parent = this
        children.add(other)
    }
}

class Leaf<T>(val data: T, parent: Node<T>? = null) : Node<T>(parent) {
}

