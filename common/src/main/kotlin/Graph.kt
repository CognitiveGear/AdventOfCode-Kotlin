import java.util.*
import kotlin.Comparator
import kotlin.collections.HashSet

interface Graph<T> {

    fun neighbors(pos: T) : Set<T>
    fun edgeCost(edge: Pair<T, T>) : Int
    fun vertexCost(pos : T) : Int
    operator fun get(pos: T) = neighbors(pos)
    operator fun get(edge: Pair<T, T>) = edgeCost(edge)

    fun depthFirst(
        start : T,
        comparator: Comparator<T>,
        endCondition : (T) -> Boolean
    ) : T {
        val visited = HashSet<T>()
        val allPos = PriorityQueue(comparator)
        allPos.add(start)
        var current : T = start
        while (allPos.isNotEmpty()) {
            current = allPos.poll()
            visited.add(current)
            if (endCondition(current)) {
                return current
            }
            val frontier = neighbors(current).filter { it !in visited }
            visited.addAll(frontier)
            allPos.addAll(frontier)
        }
        return current
    }

    fun dijkstra(start: T, endCondition: (T) -> Boolean) : Dijkstra<T> = Dijkstra(start, this, endCondition)

    fun reduced(subSet : Set<T>) : FiniteGraph<T> {
        val newNeighbors : Map<T, Set<T>> = buildMap {
            subSet.forEach { start ->
                set(start, subSet.filterTo(HashSet()) {it != start} )
            }
        }
        val newEdgeCost = buildMap {
            subSet.forEach { start ->
                val ends: Set<T> = subSet.filterTo(HashSet()) { it != start }
                val mutEnds = ends.toMutableSet()
                val dijkstra = dijkstra(
                    start,
                ) {
                    mutEnds.remove(it)
                    mutEnds.isEmpty()
                }
                ends.forEach {
                    set(start to it, dijkstra.distanceTo[it]!!)
                }
            }
        }
        val newVertexCost = buildMap {
            subSet.forEach {
                set(it, vertexCost(it))
            }
        }
        return FiniteGraph(
            newNeighbors,
            newEdgeCost,
            newVertexCost
        )
    }
}