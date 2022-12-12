import java.util.PriorityQueue

@Suppress("MemberVisibilityCanBePrivate", "unused")
class BreadthFirst<T>(
    startPos : T,
    comparator: Comparator<List<T>>,
    getNeighbors : (T) -> Sequence<T>,
    endCondition : (T) -> Boolean,
) {
    private val startPath = listOf(startPos)
    private val paths: PriorityQueue<List<T>> = PriorityQueue(comparator)
    private val _shortestPathTo: MutableMap<T, List<T>> = mutableMapOf(startPos to startPath)
    val shortestPathTo : Map<T, List<T>>
        get() = _shortestPathTo.toMap()
    val bestPath : List<T>
    init {
        var currentPath = startPath
        while (!endCondition(currentPath.last())) {
            val currentPos = currentPath.last()
            paths.addAll(
                getNeighbors(currentPos).map {
                    currentPath + it
                }.filter {
                    _shortestPathTo[it.last()]?.let { memoPath ->
                        if (comparator.compare(it, memoPath) < 0) {
                            _shortestPathTo[it.last()] = it
                            true
                        } else {
                            false
                        }
                    } ?: run {
                        _shortestPathTo[it.last()] = it
                        true
                    }
                }
            )
            currentPath = paths.poll()
        }
        paths.add(currentPath)
        bestPath = currentPath
    }
}