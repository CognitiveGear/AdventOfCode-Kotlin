import java.util.PriorityQueue

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Dijkstra<T> (
    startPos : T,
    getNeighbors : (T) -> Sequence<T>,
    endCondition : (T) -> Boolean,
    edgeCost : ((T, T) -> Double)? = null,
    vertexCost : ((T) -> Double)? = null,
) {
    private val distance : MutableMap<T, Double> = mutableMapOf(startPos to 0.0)
    val distanceTo : Map<T, Double>
        get() = distance
    private val previous : MutableMap<T, T> = mutableMapOf()
    val pathTree : Map<T, T>
        get() = previous
    private val frontier: PriorityQueue<T> =
        if (vertexCost != null) {
            PriorityQueue(compareBy { distance[it]!! + vertexCost(it) })
        } else {
            PriorityQueue(compareBy { distance[it]!! })
        }
    val bestPath : List<T>
    private val distanceThrough : (T, T) -> Double =
        if (edgeCost != null) {
            { current, neighbor -> distance[current]!! + edgeCost(current, neighbor) }
        } else {
            { current, _ -> distance[current]!! + 1.0 }
        }
    init {
        frontier.add(startPos)
        while (frontier.isNotEmpty() && !endCondition(frontier.peek())) {
            val current = frontier.poll()
            frontier.addAll(
                getNeighbors(current).filter { neighbor ->
                    val tentativeDistance = distanceThrough(current, neighbor)
                    (tentativeDistance < distance.getOrDefault(neighbor, Double.MAX_VALUE)).also {
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