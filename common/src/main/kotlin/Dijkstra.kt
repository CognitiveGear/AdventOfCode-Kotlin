import java.util.PriorityQueue

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Dijkstra<T>(
    startPos: T,
    graph: Graph<T>,
    endCondition: (T) -> Boolean,
) {
    private val distance : MutableMap<T, Int> = mutableMapOf(startPos to 0)
    val distanceTo : Map<T, Int>
        get() = distance
    private val previous : MutableMap<T, T> = mutableMapOf()
    val pathTree : Map<T, T>
        get() = previous
    private val frontier: PriorityQueue<T> =
        PriorityQueue(compareBy { distance[it]!! + graph.vertexCost(it) })
    val bestPath : List<T>
    private val distanceThrough : (T, T) -> Int =
        { current, neighbor -> distance[current]!! + graph.edgeCost(current to neighbor) }
    init {
        frontier.add(startPos)
        while (frontier.isNotEmpty() && !endCondition(frontier.peek())) {
            val current = frontier.poll()
            frontier.addAll(
                graph.neighbors(current).filter { neighbor ->
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
        bestPath = buildList {
            var next = frontier.peek()
            while (next != null) {
                add(next)
                next = previous[next]
            }
            reverse()
        }
    }
}