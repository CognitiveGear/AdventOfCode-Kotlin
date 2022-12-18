class Day18 : AdventDay(2022, 18) {

    val voxels = lines.map {
        Voxel(it.grabInts())
    }.toSet()

    data class Voxel(val x: Int, val y: Int, val z: Int) {

        constructor(list: List<Int>) : this(list[0], list[1], list[2])
        fun l1Neighbors() : Set<Voxel> {
            return setOf(
                Voxel(x + 1, y, z),
                Voxel(x - 1, y, z),
                Voxel(x, y + 1, z),
                Voxel(x, y - 1, z),
                Voxel(x, y, z + 1),
                Voxel(x, y, z - 1),
            )
        }
    }

    val neighbors = voxels.associateWith { it.l1Neighbors().filter { neighbor -> neighbor in voxels}.toSet() }

    override fun part1(): String {
        return neighbors.values.sumOf { 6 - it.size }.toString()
    }

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
                }.toSet()
            },
            { 1 },
            { 0 }
        )
        val (water, _) = waterGraph.depthFirst(
            Voxel(xMin, yMin, zMin),
            compareBy { it.x + it.y + it.z }
        ) { false }
        val exposedToWater = voxels
            .map { it to it.l1Neighbors().intersect(water) }
            .filter { it.second.isNotEmpty() }
            .toMap()
        return exposedToWater.values.sumOf { it.size }.toString()
    }
}

fun main() {
    Day18().main()
}