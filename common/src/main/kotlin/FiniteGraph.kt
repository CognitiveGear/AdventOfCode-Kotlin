@Suppress("MemberVisibilityCanBePrivate", "unused")
class FiniteGraph<T>(
    val neighborMap: Map<T, List<T>>,
    val edgeCostMap: Map<Pair<T, T>, Int>? = null,
    val vertexCostMap: Map<T, Int>? = null
) : Graph<T> {
    override fun neighbors(pos: T): Sequence<T> = neighborMap.getValue(pos).asSequence()
    override val hasEdgeCost = edgeCostMap != null
    override fun edgeCost(edge: Pair<T, T>) = edgeCostMap?.getValue(edge) ?: 0
    override val hasVertexCost = vertexCostMap != null
    override fun vertexCost(pos: T): Int = vertexCostMap?.getValue(pos) ?: 0

    fun reduced(filterOp : (T) -> Boolean) : FiniteGraph<T> {
        return reduced(neighborMap.keys.filterTo(HashSet(), filterOp))
    }
}