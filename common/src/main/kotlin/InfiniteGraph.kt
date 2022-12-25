class InfiniteGraph<T>(
    private val getNeighbors: (T) -> Sequence<T>,
    private val getEdgeCost: ((Pair<T, T>) -> Int)? = null,
    private val getVertexCost: ((T) -> Int)? = null
) : Graph<T> {
    override fun neighbors(pos: T): Sequence<T> = getNeighbors(pos)
    override val hasEdgeCost = getEdgeCost != null
    override fun edgeCost(edge: Pair<T, T>): Int = getEdgeCost?.invoke(edge) ?: 0
    override val hasVertexCost = getVertexCost != null
    override fun vertexCost(pos: T): Int = getVertexCost?.invoke(pos) ?: 0
}