class Day08 : AdventDay(2022, 8) {

    private val maxRow : Int
    private val maxCol : Int

    private val treeGrid : List<List<Int>>
    init {
        lines.let {
            maxRow = it.size - 1
            maxCol = it.first().length - 1
            treeGrid = List(maxRow + 1) {row ->
                List(maxCol + 1) { col ->
                    it[row][col].digitToInt()
                }
            }
        }
    }

    override fun part1(): String {
        val visible : MutableSet<Pair<Int, Int>> = mutableSetOf()
        fun IntRange.visibilitySweep(coordMap : (Int) -> Pair<Int, Int>) {
            listOf(this, this.reversed()).forEach range@ { range ->
                var lastTree = -1
                range.forEach { index ->
                    val (row, col) = coordMap(index)
                    val tree = treeGrid[row][col]
                    if (tree > lastTree) {
                        lastTree = tree
                        visible.add(row to col)
                    }
                    if (tree >= 9) {
                        return@range
                    }
                }
            }
        }
        (0..maxRow).forEach { row ->
            (0..maxCol).visibilitySweep { row to it }
        }
        (0..maxCol).forEach { col ->
            (0..maxRow).visibilitySweep { it to col }
        }
        return visible.count().toString()
    }

    override fun part2(): String {
        return treeGrid.withIndex().maxOf Row@ { (row, gridRow) ->
            gridRow.withIndex().maxOf Col@ { (col, tree) ->
                if (row == 0 || row == maxRow || col == 0 || col == maxCol) {
                    return@Col 0
                }
                var score = 1
                for (left in (col - 1) downTo 0) {
                    if (treeGrid[row][left] >= tree || left == 0) {
                        score *= col - left
                        break
                    }
                }
                for (right in (col + 1)..maxCol) {
                    if (treeGrid[row][right] >= tree || right == maxCol) {
                        score *= right - col
                        break
                    }
                }
                for (up in (row - 1) downTo 0) {
                    if (treeGrid[up][col] >= tree || up == 0) {
                        score *= row - up
                        break
                    }
                }
                for (down in (row + 1)..maxRow) {
                    if (treeGrid[down][col] >= tree || down == maxRow) {
                        score *= down - row
                        break
                    }
                }
                score
            }
        }.toString()
    }

}

fun main() {
    Day08().main()
}