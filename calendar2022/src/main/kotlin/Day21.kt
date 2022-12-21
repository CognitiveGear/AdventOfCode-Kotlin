import java.util.*
import kotlin.math.roundToLong

class Day21 : AdventDay(2022, 21) {

    enum class Operation {
        PLUS,
        MINUS,
        TIMES,
        DIVIDE
    }

    data class Variable(val scalar: Double, val linear: Double) {
        operator fun plus(arg: Variable) : Variable = Variable(scalar + arg.scalar, linear + arg.linear)
        operator fun minus(arg: Variable) : Variable = Variable(scalar - arg.scalar, linear - arg.linear)
        operator fun times(arg: Variable) : Variable =
            Variable(
                scalar * arg.scalar - linear * arg.linear,
                linear * arg.scalar + scalar * arg.linear
            )
        operator fun div(arg: Variable) : Variable {
            val denominator = arg.scalar * arg.scalar + arg.linear * arg.linear
            return Variable(
                (scalar * arg.scalar + linear * arg.linear) / denominator,
                (linear * arg.scalar - scalar * arg.linear) /denominator
            )
        }
        override fun toString() = "($scalar + ${linear}x)"
    }

    sealed class Job
    class YellVariable(val variable: Variable) : Job() {
        constructor(scalar: Double, linear: Double) : this(Variable(scalar, linear))
    }
    class YellOperation(val first: String, val second: String, val operation: Operation) : Job() {

        operator fun invoke(first: Variable, second: Variable) : Variable {
            return when (operation) {
                Operation.PLUS -> { first + second}
                Operation.MINUS -> { first - second }
                Operation.TIMES -> { first * second }
                Operation.DIVIDE -> { first / second }
            }
        }
    }

    val inputMonkeys: Map<String, Job> = lines.associate {
        val key = it.substringBefore(':')
        val jobString = it.substringAfter(' ')
        val job : Job = if (jobString.first().isDigit()) {
            YellVariable(jobString.toDouble(), 0.0)
        } else {
            val first = jobString.substring(0..3)
            val second = jobString.substring(7..10)
            val op = when(jobString[5]) {
                '+' -> Operation.PLUS
                '-' -> Operation.MINUS
                '*' -> Operation.TIMES
                '/' -> Operation.DIVIDE
                else -> throw IllegalArgumentException("malformed input")
            }
            YellOperation(first, second, op)
        }
        key to job
    }


    fun iterateMonkeyOperations(
        key: String,
        monkeys: Map<String, Job>,
        monkeyValues : MutableMap<String, Variable>,
    ) : Variable {
        val resultMaybe = monkeyValues[key]
        if (resultMaybe != null) {
            return resultMaybe
        }
        var result = Variable(0.0, 0.0)
        val jobStack = Stack<String>()
        jobStack.push(key)
        while (jobStack.isNotEmpty()) {
            val current = jobStack.pop()
            when (val job = monkeys[current]!!) {
                is YellVariable -> result = job.variable
                is YellOperation -> {
                    val firstNum = monkeyValues[job.first]
                    val secondNum = monkeyValues[job.second]
                    if (firstNum != null && secondNum != null) {
                        result = job(firstNum, secondNum)
                        println("${job.first}:$firstNum ${job.operation.name} ${job.second}:$secondNum -> $result")
                        monkeyValues[current] = result
                    } else {
                        jobStack.push(current)
                        if (firstNum == null) {
                            jobStack.push(job.first)
                        }
                        if (secondNum == null) {
                            jobStack.push(job.second)
                        }
                    }
                }
            }
        }
        return result
    }

    override fun part1(): String {
        val monkeyValues : MutableMap<String, Variable> = inputMonkeys
            .filterValues { it is YellVariable }
            .mapValues { (it.value as YellVariable).variable }
            .toMutableMap()
        return iterateMonkeyOperations("root", inputMonkeys, monkeyValues).scalar.roundToLong().toString()
    }

    override fun part2(): String {
        val oldRoot = inputMonkeys["root"]!! as YellOperation
        val part2Monkeys = inputMonkeys.filterKeys { it != "root" && it != "humn"} +
                ("root" to YellOperation(oldRoot.first, oldRoot.second, Operation.MINUS)) +
                ("humn" to YellVariable(Variable(0.0, 1.0)))
        val part2MonkeyValues = part2Monkeys
            .filterValues { it is YellVariable }
            .mapValues { (it.value as YellVariable).variable }
            .toMutableMap()
        val rootValue = iterateMonkeyOperations("root", part2Monkeys, part2MonkeyValues)
        println(rootValue)
        return (-rootValue.scalar / rootValue.linear).roundToLong().toString()
    }
}

fun main() {
    Day21().main()
}