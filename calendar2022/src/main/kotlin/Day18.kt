class Day18 : AdventDay(2022, 18) {

    val voxels = lines.map {
        Voxel(it.grabInts())
    }.toSet()

    val neighbors = voxels.associateWith { it.l1Neighbors().filterTo(HashSet()) { neighbor -> neighbor in voxels} }

    override fun part1(): String = neighbors.values.sumOf { 6 - it.size }.toString()

    override fun part2(): String {
        val xMax = voxels.maxOf { it.x } + 1
        val xMin = voxels.minOf { it.x } - 1
        val yMin = voxels.minOf{ it.y } - 1
        val yMax = voxels.maxOf{ it.y } + 1
        val zMin = voxels.minOf{ it.z } - 1
        val zMax = voxels.maxOf{ it.z } + 1
        val xBound = xMin..xMax
        val yBound = yMin..yMax
        val zBound = zMin..zMax
        val waterGraph = InfiniteGraph<Voxel> (
            { current ->
                current.l1Neighbors().filter { next ->
                    next.x in xBound && next.y in yBound && next.z in zBound && next !in voxels
                }
            }
        )
        val (water, _) = waterGraph.searchBy(
            Voxel(xMin, yMin, zMin),
            compareBy { it.x + it.y + it.z }
        ) { false }
        val exposedToWater = voxels
            .map { it to it.l1Neighbors().toSet().intersect(water) }
            .filter { it.second.isNotEmpty() }
            .toMap()
        return exposedToWater.values.sumOf { it.size }.toString()
    }
}

fun main() {
    Day18().main()
}