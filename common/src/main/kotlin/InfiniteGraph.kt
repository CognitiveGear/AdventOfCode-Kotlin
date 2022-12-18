class InfiniteGraph<T>(
    val getNeighbors : (T) -> Set<T>,
    val getEdgeCost: (Pair<T, T>) -> Int,
    val getVertexCost: (T) -> Int
) : Graph<T> {

    override fun neighbors(pos: T): Set<T> = getNeighbors(pos)
    override fun edgeCost(edge: Pair<T, T>): Int = getEdgeCost(edge)
    override fun vertexCost(pos: T): Int = getVertexCost(pos)

}