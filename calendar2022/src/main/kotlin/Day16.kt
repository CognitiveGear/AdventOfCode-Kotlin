import kotlin.math.min

class Day16 : AdventDay(2022, 16) {

    val valveFlowRate : Map<String, Int>
    private val start = "AA"
    val tapGraph : FiniteGraph<String>
    private val usefulPos : Set<String>

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
        val valveConnectedTo = processedLines.associate { it.first to it.second.second }
        usefulPos = valveFlowRate.filterValues { it > 0 }.keys + start
        tapGraph = InfiniteGraph (
            valveConnectedTo::getValue,
            { 1 },
            { 0 }
        ).reduced(usefulPos)
    }

    data class PathData (
        val time: Int,
        val rate : Int,
        val flow : Int,
    )

    val pathMemo = mutableMapOf("AA" to PathData(0, 0, 0))

    inner class Path(private val path: String) {
        val timeLength : Int
            get() = pathMemo[path]!!.time
        private val totalFlow : Int
            get() = pathMemo[path]!!.flow
        private val flowRate : Int
            get() = pathMemo[path]!!.rate
        val last : String
            get() = path.takeLast(2)

        operator fun contains(pos : String) = pos in path
        operator fun plus(new: String) : Path {
            val newPath = "$path:$new"
            if (!pathMemo.containsKey(newPath)) {
                val old = pathMemo[path]!!
                val deltaT = tapGraph[last to new] + 1
                pathMemo[newPath] = PathData(
                    old.time + deltaT,
                    old.rate + valveFlowRate[new]!!,
                    old.flow + old.rate * deltaT
                )
            }
            return Path(newPath)
        }
        operator fun compareTo(arg: Path) = path.compareTo(arg.path)
        fun sum(maxTime : Int) : Int {
            return flowRate * (maxTime - timeLength) + totalFlow
        }
        override fun toString(): String = path
    }


    override fun part1(): String {
        val pathGraph = InfiniteGraph(
            { path : Path ->
                tapGraph[path.last]
                    .asSequence()
                    .filter { it !in path }
                    .map { path + it }
                    .filter { it.timeLength <= 30.0 }
                    .toList()
            },
            { pair -> tapGraph[pair.first.last to pair.second.last] + 1 },
            { 0 }
        )
        val startPath = Path(start)
        var bestPath = startPath
        pathGraph.searchBy(
            startPath,
            compareByDescending {
                path -> path.timeLength
            }
        ) {
            if (it.sum(30) > bestPath.sum(30)) {
                bestPath = it
            }
            false
        }
        println(bestPath)
        return bestPath.sum(30).toString()
    }

    override fun part2(): String {
        val pairGraph = InfiniteGraph(
            { (h, e) ->
                tapGraph[h.last]
                    .asSequence()
                    .filter { it !in h && it !in e }
                    .map { h + it }
                    .filter { it.timeLength <= 26.0 }
                    .flatMap { newH ->
                        tapGraph[e.last]
                            .asSequence()
                            .filter { it !in newH && it !in e }
                            .map { e + it }
                            .filter { it.timeLength <= 26.0 }
                            .map { newE ->
                                newH to newE
                            }
                    }.toList()
            },
            { edgePair: Pair<Pair<Path, Path>, Pair<Path, Path>> ->
                val (old, new) = edgePair
                tapGraph[old.first.last to new.first.last] + tapGraph[old.second.last to new.second.last]
            },
            { 0 }
        )
        val startPair = Pair(Path(start), Path(start))
        var bestPair = startPair
        fun Pair<Path, Path>.sum26() : Int {
            return first.sum(26) + second.sum(26)
        }
        pairGraph.searchBy(
            startPair,
            compareByDescending {
                min(it.first.timeLength, it.second.timeLength)
            },
        ) {
            if (it.sum26() > bestPair.sum26()) {
                bestPair = it
            }
            false
        }
        println(bestPair)
        return bestPair.sum26().toString()
    }
}

fun main() {
    Day16().main()
}
