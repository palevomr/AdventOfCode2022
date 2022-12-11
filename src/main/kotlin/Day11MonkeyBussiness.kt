import java.io.File
import java.util.LinkedList
import java.util.Queue

class Day11MonkeyBusiness {

    data class Monkey(
        val items: Queue<Long> = LinkedList(),
        var operation: (Long) -> Long = { 0 },
        var testDivider: Long = -1,
        var targetIfPositive: Int = -1,
        var targetIfNegative: Int = -1,
        var monkeyBusiness: Long = 0
    ) {
        override fun toString(): String {
            return "$items, mb = $monkeyBusiness, $testDivider, true -> $targetIfPositive, false - $targetIfNegative"
        }
    }

    private val monkeys = mutableListOf<Monkey>()

    fun addMonkey(monkey: Monkey) {
        monkeys.add(monkey)
    }

    fun startGame(rounds: Int = 20) {
        val wrap = monkeys.map { it.testDivider }.reduce { acc, l -> acc * l }
        repeat(rounds) {
            monkeys.forEachIndexed { idx, monkey ->
                while (monkey.items.isNotEmpty()) {
                    val current = monkey.items.poll()
                    val operationValue = monkey.operation(current)
                    val operationValueDivided = operationValue % wrap
                    if (operationValueDivided % monkey.testDivider == 0L) {
                        monkeys[monkey.targetIfPositive].items.offer(operationValueDivided)
                    } else {
                        monkeys[monkey.targetIfNegative].items.offer(operationValueDivided)
                    }
                    monkey.monkeyBusiness++
                }
            }
            monkeys.forEachIndexed { idx, it ->
                println("$idx $it")
            }
            println()
        }

    }

    fun calculateTopMonkeyBusiness(): Long {
        val monkeyBusiness = monkeys.map { it.monkeyBusiness }.sortedDescending()
        return monkeyBusiness[0] * monkeyBusiness[1]
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_11")
            val monkeyBusiness = Day11MonkeyBusiness()
            var currentMonkey: Monkey? = null
            file.forEachLine {
                val input = it.trim()
                when {
                    input.startsWith("Monkey") -> {
                        currentMonkey = Monkey().also {
                            monkeyBusiness.addMonkey(it)
                        }
                    }

                    input.startsWith("Starting") -> {
                        val items = input.takeLastWhile { it != ':' }.trim().split(", ").map { it.toLong() }
                        currentMonkey?.items?.addAll(items)
                    }

                    input.startsWith("Operation") -> {
                        val operationString = input.drop(21)
                        val operand = operationString[0]
                        val valueString = operationString.takeLastWhile { it != ' ' }
                        currentMonkey?.operation = {
                            val value = valueString.toLongOrNull() ?: it
                            if (operand == '+') {
                                it + value
                            } else {
                                it * value
                            }
                        }
                    }

                    input.startsWith("Test") -> {
                        currentMonkey?.testDivider = input.takeLastWhile { it != ' ' }.toLong()
                    }

                    input.startsWith("If true") -> {
                        currentMonkey?.targetIfPositive = input.takeLastWhile { it != ' ' }.toInt()
                    }

                    input.startsWith("If false") -> {
                        currentMonkey?.targetIfNegative = input.takeLastWhile { it != ' ' }.toInt()
                    }
                }
            }
            monkeyBusiness.startGame(10000)
            println(monkeyBusiness.calculateTopMonkeyBusiness())
        }

    }
}