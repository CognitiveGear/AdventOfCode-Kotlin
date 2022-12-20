class Day19: AdventDay(2022, 19) {

    val oreDelta = listOf(1, 0, 0, 0)
    val clayDelta = listOf(0, 1, 0, 0)
    val obsiDelta = listOf(0, 0, 1, 0)
    val geodDelta = listOf(0, 0, 0, 1)
    data class Robot(val cost: List<Int>, val production: List<Int>)
    data class BluePrint (
        val robots : List<Robot>
    ) : List<Robot> by robots {

        val maxOreCost = robots.drop(1).maxOf { it.cost[0] }
        val maxClayCost = robots.maxOf { it.cost[1] }
        val maxObsiCost = robots.maxOf { it.cost[2] }
    }

    val blueprints = lines.map {
        val numbers = it.grabInts()
        BluePrint(
            listOf(
                Robot(listOf(numbers[1], 0, 0, 0), oreDelta),
                Robot(listOf(numbers[2], 0, 0, 0), clayDelta),
                Robot(listOf(numbers[3], numbers[4], 0, 0), obsiDelta),
                Robot(listOf(numbers[5], 0, numbers[6], 0), geodDelta)
            )
        )
    }

    data class Factory (
        val time: Int,
        val stock: List<Int>,
        val robots: List<Int>
    ) {
        companion object {
            val empty = listOf(0, 0, 0, 0)
            val emptyRobot = Robot(empty, empty)
        }

        enum class DO { ORE, CLAY, OBSI, GEOD, WAIT }

        operator fun List<Int>.plus(arg: List<Int>) : List<Int> {
            return List(4) { this[it] + arg[it] }
        }

        operator fun List<Int>.minus(arg: List<Int>) : List<Int> {
            return List(4) { this[it] - arg[it] }
        }

        operator fun List<Int>.compareTo(arg: List<Int>) : Int {
            val zipper = this.zip(arg)
            return when {
                zipper.any { it.first < it.second } -> -1
                zipper.all { it.first == it.second } -> 0
                else -> 1
            }
        }

        fun canDo(act: DO, blueprint: BluePrint) : Boolean {
            return when (act) {
                DO.ORE -> stock[0] >= blueprint[0].cost[0]
                DO.CLAY -> stock[0] >= blueprint[1].cost[0]
                DO.OBSI -> stock[0] >= blueprint[2].cost[0] && stock[1] >= blueprint[2].cost[1]
                DO.GEOD -> stock[0] >= blueprint[3].cost[0] && stock[2] >= blueprint[3].cost[2]
                DO.WAIT -> true
            }
        }

        fun shouldDo(blueprint: BluePrint) : List<Factory> {
            return buildList {
                // Build, but don't overbuild, ore robots
                if (canDo(DO.ORE, blueprint) && robots[0] < blueprint.maxOreCost) {
                    add(act(DO.ORE, blueprint))
                }
                // Build, but don't overbuild, clay robots
                if (canDo(DO.CLAY, blueprint) && robots[1] < blueprint.maxClayCost) {
                    add(act(DO.CLAY, blueprint))
                }
                // If you're at tier 1, and you have all options available, make a choice, don't wait
                if (robots[1] == 0) {
                    if (size < 2) {
                        add(act(DO.WAIT, blueprint))
                    }
                    return@buildList
                }
                // Build, but don't overbuild, obsidian robots
                if (canDo(DO.OBSI, blueprint) && robots[2] < blueprint.maxObsiCost) {
                    add(act(DO.OBSI, blueprint))
                }
                // If you're at tier 2, and you have all options available, make a choice, don't wait
                if (robots[2] == 0) {
                    if (size < 3) {
                        add(act(DO.WAIT, blueprint))
                    }
                    return@buildList
                }
                if (canDo(DO.GEOD, blueprint)) {
                    add(act(DO.GEOD, blueprint))
                    if (size < 4) {
                        add(act(DO.WAIT, blueprint))
                    }
                    return@buildList
                }
                add(act(DO.WAIT, blueprint))
            }
        }

        fun act(act: DO, blueprint: BluePrint) : Factory {
            val newRobot = when (act) {
                DO.ORE -> blueprint.robots[0]
                DO.CLAY -> blueprint.robots[1]
                DO.OBSI -> blueprint.robots[2]
                DO.GEOD -> blueprint.robots[3]
                DO.WAIT -> emptyRobot
            }
            return Factory(
                time = time + 1,
                stock = stock + robots - newRobot.cost,
                robots = robots + newRobot.production
            )
        }

        override fun toString(): String {
            return "t: $time\t\ts: $stock\t\tr: $robots\n"
        }
    }

    val gameGraphs = blueprints.map {
        InfiniteGraph<Factory>(
            getNeighbors = { factory -> factory.shouldDo(it) },
            { 1 },
            { -it.stock[3] }
        )
    }

    val startStock = Factory.empty
    val startDelta = listOf(1, 0, 0, 0)
    val gameStart = Factory(0, startStock, startDelta)

    fun endCostLowerBound(maxTime: Int, it: Factory) : Int {
        val timeLeft = maxTime - it.time
        return -it.stock[3] - it.robots[3] * timeLeft - timeLeft * (timeLeft - 1) / 2
    }

    fun endCostUpperBound(maxTime: Int, it: Factory) : Int {
        val timeLeft = maxTime - it.time
        return -it.stock[3] - it.robots[3] * timeLeft
    }

    override fun part1(): String {
        val maxGeodes = gameGraphs.map { graph ->
            graph.endCostMinimization(
                start = gameStart,
                endCostLowerBound = { endCostLowerBound(24, it) },
                endCostUpperBound = { endCostUpperBound(24, it) }
            ) { state ->
                state.time == 24
            }
        }
        return maxGeodes.mapIndexed { index, result ->
            (index + 1) * result.best.last().stock[3]
        }.sum().toString()
    }

    override fun part2(): String {
        val paths = gameGraphs.take(3).map { game ->
            game.endCostMinimization(
                start = gameStart,
                endCostLowerBound = { endCostLowerBound(32, it) },
                endCostUpperBound = { endCostUpperBound(32, it) }
            ) { it.time == 32 }
        }.map { it.best }
        return paths.fold(1) { acc, value -> acc * value.last().stock[3] }.toString()
    }
}

fun main() {
    Day19().main()
}