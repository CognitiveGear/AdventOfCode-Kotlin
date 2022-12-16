import java.lang.Integer.min
import java.util.*

class Day16 : AdventDay(2022, 16) {

    val valveFlowRate : Map<String, Int>
    val valveConnectedTo: Map<String, List<String>>
    val usefulTaps : Set<String>
    val usefulTapsTransits : Map<Pair<String, String>, Int>
    val start = "AA"

    init {
        val regex = """([A-Z][A-Z])|(\d+)""".toRegex()
        val processedLines = lines.map { line ->
            val matches = regex.findAll(line).map { it.value }.toList()
            val valve = matches[0]
            val flowRate = matches[1].toInt()
            val connectedTo = matches.drop(2)
            valve to (flowRate to connectedTo)
        }
        valveFlowRate = processedLines.associate { it.first to it.second.first }
        valveConnectedTo = processedLines.associate { it.first to it.second.second }
        usefulTaps = valveFlowRate.filterValues { it > 0 }.keys.toSet()
        usefulTapsTransits = buildMap {
            usefulTaps.forEach { left ->
                val end = usefulTaps.filter { !containsKey(it to left) && it != left } + start
                val mutEnd = end.toMutableSet()
                val dijkstra = Dijkstra(
                    startPos = left,
                    getNeighbors = { valveConnectedTo[it]!!.asSequence() },
                    endCondition = {
                        mutEnd.remove(it)
                        mutEnd.isEmpty()
                    }
                )
                end.forEach { right->
                    val distance = dijkstra.distanceTo[right]!!.toInt()
                    set(left to right, distance)
                    set(right to left, distance)
                }
            }
        }
    }

    data class PathMemo (
        val time: Int,
        val rate : Int,
        val flow : Int,
    )

    val pathMemo = mutableMapOf("AA" to PathMemo(0, 0, 0))

    inner class Path(val path: String) {

        val timeLength : Int
            get() = pathMemo[path]!!.time

        val totalFlow : Int
            get() = pathMemo[path]!!.flow

        val flowRate : Int
            get() = pathMemo[path]!!.rate

        operator fun contains(pos : String) = pos in path
        operator fun plus(new: String) : Path {
            val newPath = "$path:$new"
            if (!pathMemo.containsKey(newPath)) {
                val old = pathMemo[path]!!
                val deltaT = usefulTapsTransits[path.takeLast(2) to new]!! + 1
                pathMemo[newPath] = PathMemo(
                    old.time + deltaT,
                    old.rate + valveFlowRate[new]!!,
                    old.flow + old.rate * deltaT
                )
            }
            return Path(newPath)
        }

        fun sum(maxTime : Int) : Int {
            return flowRate * (maxTime - timeLength) + totalFlow
        }

        override fun toString(): String = path
    }


    override fun part1(): String {
        val allPaths = PriorityQueue<Path>(compareByDescending { path -> path.timeLength })
        val startPath = Path(start)
        allPaths.add(startPath)
        var bestPath = startPath
        while (allPaths.isNotEmpty()) {
            val current = allPaths.poll()
            usefulTaps.filter {it !in current}.forEach {
                val newPath = current + it
                if (newPath.timeLength <= 30) {
                    allPaths.add(newPath)
                    if (newPath.sum(30) > bestPath.sum(30)) {
                        bestPath = newPath
                    }
                }
            }
        }
        println(bestPath)
        return bestPath.sum(30).toString()
    }

    override fun part2(): String {
        val allPairPaths = PriorityQueue<Pair<Path, Path>>(
            compareByDescending {
                min(it.first.timeLength, it.second.timeLength)
            }
        )
        val startPair = Pair(Path(start), Path(start))
        var bestPair = startPair
        allPairPaths.add(startPair)
        fun Pair<Path, Path>.sum26() : Int {
            return first.sum(26) + second.sum(26)
        }
        while (allPairPaths.isNotEmpty()) {
            val current = allPairPaths.poll()
            val nextTaps = usefulTaps.filter { it !in current.first && it !in current.second }.asSequence()
            val h = current.first
            val e = current.second
            nextTaps.forEach { left ->
                nextTaps
                    .filter{ it != left }
                    .map { (h + left) to (e + it) }
                    .filter { it.first.timeLength <= 26 && it.second.timeLength <= 26}
                    .forEach { newPair ->
                        allPairPaths.add(newPair)
                        if (newPair.sum26() > bestPair.sum26()) {
                            bestPair = newPair
                        }
                }
            }
        }
        println(bestPair)
        return (bestPair.first.sum(26) + bestPair.second.sum(26)).toString()
    }
}

fun main() {
    Day16().main()
}
