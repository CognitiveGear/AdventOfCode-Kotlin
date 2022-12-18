import java.util.*
import kotlin.Comparator
import kotlin.collections.HashSet

interface Graph<T> {

    fun neighbors(pos: T) : Set<T>
    fun edgeCost(edge: Pair<T, T>) : Int
    fun vertexCost(pos : T) : Int
    operator fun get(pos: T) = neighbors(pos)
    operator fun get(edge: Pair<T, T>) = edgeCost(edge)

    data class DepthFirstResult<T>(
        val visited: Set<T>,
        val end: T
    )

    data class DijkstraResult<T>(
        val distance: Map<T, Int>,
        val previous: Map<T, T>,
        val bestPath: List<T>
    )

    fun depthFirst(
        start : T,
        comparator: Comparator<T>,
        endCondition : (T) -> Boolean
    ) : DepthFirstResult<T> {
        val visited = HashSet<T>()
        val allPos = PriorityQueue(comparator)
        allPos.add(start)
        var current : T = start
        visited.add(current)
        while (allPos.isNotEmpty()) {
            current = allPos.poll()
            if (endCondition(current)) {
                return DepthFirstResult(visited, current)
            }
            val frontier = neighbors(current).filter { it !in visited }
            visited.addAll(frontier)
            allPos.addAll(frontier)
        }
        return DepthFirstResult(visited, current)
    }

    fun dijkstra(start: T, endCondition: (T) -> Boolean) : DijkstraResult<T> {
        val distance : MutableMap<T, Int> = mutableMapOf(start to 0)
        val previous : MutableMap<T, T> = mutableMapOf()
        val frontier: PriorityQueue<T> =
            PriorityQueue(compareBy { distance[it]!! + vertexCost(it) })
        val distanceThrough : (T, T) -> Int =
            { current, neighbor -> distance[current]!! + edgeCost(current to neighbor) }
        frontier.add(start)
        while (frontier.isNotEmpty() && !endCondition(frontier.peek())) {
            val current = frontier.poll()
            frontier.addAll(
                neighbors(current).filter { neighbor ->
                    val tentativeDistance = distanceThrough(current, neighbor)
                    (tentativeDistance < distance.getOrDefault(neighbor, Int.MAX_VALUE)).also {
                        if (it) {
                            distance[neighbor] = tentativeDistance
                            previous[neighbor] = current
                        }
                    }
                }
            )
        }
        if (frontier.isEmpty()) {
            throw IllegalStateException("Cannot reach end goal, end condition failed for all nodes.")
        }
        val bestPath = buildList {
            var next = frontier.peek()
            while (next != null) {
                add(next)
                next = previous[next]
            }
            reverse()
        }.toList()
        return DijkstraResult(distance, previous, bestPath)
    }

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
                    set(start to it, dijkstra.distance[it]!!)
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