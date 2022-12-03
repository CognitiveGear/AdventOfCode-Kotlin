import org.junit.jupiter.api.Test

open class AdventDayTest(
    private val day: AdventDay,
    private val part1TestAnswer: String,
    private val part2TestAnswer: String
) {
    @Test
    fun testPart1() {
        val result = day.part1()
        println(result)
        assert(result == part1TestAnswer)
    }

    @Test
    fun testPart2() {
        val result = day.part2()
        println(result)
        assert(result == part2TestAnswer)
    }
}