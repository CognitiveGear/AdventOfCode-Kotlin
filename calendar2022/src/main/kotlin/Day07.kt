class Day07 : AdventDay(2022, 7) {

    private inner class Dir (
        val name : String,
        val parent : Dir?,
        val files : MutableMap<String, Long> = mutableMapOf(),
        val subDir : MutableMap<String, Dir> = mutableMapOf(),
    ) {
        val totalSize : Long by lazy { files.values.sum() + subDir.values.sumOf { it.totalSize } }
        operator fun contains(n: String) : Boolean = n in subDir
        operator fun get(n: String) : Dir = subDir[n]!!
        fun addDir(n: String) : Dir = subDir[n] ?: run {
            Dir(n, this).also {
                subDir += (n to it)
                allDirs += it
            }
        }
        operator fun plus(p: Pair<String, Long>) { files += p }
        override fun toString() : String = "($name, $totalSize)"

    }

    private val fs = Dir("/", null, mutableMapOf(), mutableMapOf())

    private var allDirs = mutableListOf(fs)

    init {
        var currentDir = fs
        lines.forEach { line ->
            when (line.take(3)) {
                "$ c" -> {
                    line.drop(5).let {
                        currentDir = when (it) {
                            ".." -> currentDir.parent ?: fs
                            "/" -> fs
                            else -> currentDir.addDir(it)
                        }
                    }
                }
                "$ l" -> {}
                else -> {
                    val (type, name) = line.split(' ')
                    when (type) {
                        "dir" -> currentDir.addDir(name)
                        else -> currentDir + (name to type.toLong())
                    }
                }
            }
        }
        fs.totalSize
        allDirs.sortBy { it.totalSize }
    }

    override fun part1() : String {
        return allDirs.takeWhile { it.totalSize < 100000 }.sumOf { it.totalSize }.toString()
    }

    override fun part2() : String {
        val maxSize = 40000000
        val toRemove = fs.totalSize - maxSize
        val removeDir = allDirs.find { it.totalSize > toRemove }
        return removeDir?.totalSize.toString()
    }
}

fun main() {
    Day07().main()
}