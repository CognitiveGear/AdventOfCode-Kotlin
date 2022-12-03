abstract class AdventDay(fileName: String) {

    protected val input = readInput(fileName)
    abstract fun part1() : String
    abstract fun part2() : String
    fun run() {
        println(part1())
        println(part2())
    }
}