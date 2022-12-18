@Suppress("MemberVisibilityCanBePrivate")
class FiniteGraph<T>(
    val neighborMap: Map<T, Set<T>>,
    val edgeCostMap: Map<Pair<T, T>, Int>,
    val vertexCostMap: Map<T, Int>?,
) : Graph<T> {
    override fun neighbors(pos: T): Set<T> = neighborMap.getValue(pos)
    override fun edgeCost(edge: Pair<T, T>) = edgeCostMap.getValue(edge)
    override fun vertexCost(pos: T): Int = vertexCostMap?.getValue(pos) ?: 0

    override fun toString() : String {
        return neighborMap.keys.joinToString("\n") { left ->
            neighbors(left).joinToString(
                ", ",
                "$left: "
            ) { "$it (${edgeCostMap[left to it]})" }
        }
    }
}