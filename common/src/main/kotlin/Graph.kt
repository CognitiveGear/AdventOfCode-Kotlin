import java.util.*
import kotlin.Comparator
import kotlin.collections.HashSet

interface Graph<T> {
    fun neighbors(pos: T) : List<T>
    val hasEdgeCost: Boolean
    fun edgeCost(edge: Pair<T, T>) : Int
    val hasVertexCost: Boolean
    fun vertexCost(pos : T) : Int
    operator fun get(pos: T) = neighbors(pos)
    operator fun get(edge: Pair<T, T>) = edgeCost(edge)

    data class Search<T>(
        val visited: Set<T>,
        val end: T
    )

    data class Dijkstra<T>(
        val distance: Map<T, Int>,
        val previous: Map<T, T>,
        val best: List<T>
    )

    data class EndCostMin<T>(
        val previous: Map<T, T>,
        val minCost: Int,
        val best: List<T>
    )

    fun searchBy(
        start : T,
        comparator: Comparator<T>,
        endCondition : (T) -> Boolean
    ) : Search<T> {
        val visited = HashSet<T>()
        val allPos = PriorityQueue(comparator)
        allPos.add(start)
        var current : T = start
        visited.add(current)
        while (allPos.isNotEmpty()) {
            current = allPos.poll()
            if (endCondition(current)) {
                return Search(visited, current)
            }
            val frontier = neighbors(current).filter { it !in visited }
            visited.addAll(frontier)
            allPos.addAll(frontier)
        }
        return Search(visited, current)
    }

    fun dijkstra(start: T, endCondition: (T) -> Boolean) : Dijkstra<T> {
        if (!hasEdgeCost) {
            throw IllegalStateException("Graph does not have an edge cost, cannot find shortest path.")
        }
        val distance : MutableMap<T, Int> = mutableMapOf(start to 0)
        val previous : MutableMap<T, T> = mutableMapOf()
        val frontier: PriorityQueue<T> = if (hasVertexCost) {
            PriorityQueue(compareBy { distance[it]!! + vertexCost(it) })
        } else {
            PriorityQueue(compareBy { distance[it]!! })
        }
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
        return Dijkstra(distance, previous, bestPath)
    }

    fun endCostMinimization(
        start: T,
        endCostLowerBound: (T) -> Int,
        endCostUpperBound: (T) -> Int,
        endCondition: (T) -> Boolean
    ) : EndCostMin<T> {
        if (!hasVertexCost) {
            throw IllegalStateException("Graph does not have an end cost to minimize")
        }
        val previous: MutableMap<T, T> = mutableMapOf()
        var minEnd : T = start
        var endMinSoFar : Int = endCostUpperBound(start)
        val frontier : PriorityQueue<T> =
            PriorityQueue(
                compareBy {
                    endCostUpperBound(it)
                }
            )
        frontier.add(start)
        var current: T
        while (frontier.isNotEmpty()) {
            current = frontier.poll()
            if (endCondition(current)) {
                val endCost = vertexCost(current)
                if (endCost <= endMinSoFar) {
                    minEnd = current
                    endMinSoFar = endCost
                }
            } else {
                frontier.addAll(
                    neighbors(current).filter {
                         endCostLowerBound(it) < endMinSoFar && !previous.containsKey(it)
                    }.onEach {
                        previous[it] = current
                    }
                )
            }
        }
        val best = buildList {
            var next : T? = minEnd
            while (next != null) {
                add(next)
                next = previous[next]
            }
            reverse()
        }
        return EndCostMin(
            minCost = endMinSoFar,
            previous = previous,
            best = best
        )
    }

    fun reduced(subSet : Set<T>) : FiniteGraph<T> {
        val newNeighbors : Map<T, List<T>> = buildMap {
            subSet.forEach { start ->
                set(start, subSet.filter {it != start} )
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
        return FiniteGraph(newNeighbors, newEdgeCost, newVertexCost)
    }
}