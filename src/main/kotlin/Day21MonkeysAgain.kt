import java.io.File
import kotlin.IllegalStateException
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class Day21MonkeysAgain(val map: Map<String, Operation>) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_21")
            val map = mutableMapOf<String, Operation>()
            file.forEachLine {
                val split = it.split(": ")
                if (split[0] == "humn") {
                    map[split[0]] = Operation.MyInput
                } else {
                    map[split[0]] = Operation.parse(split[1])
                }
            }
            val monkeysAgain = Day21MonkeysAgain(map)
            val root = map["root"] as Operation.Evaluation
            println(
                "${
                    measureTimeMillis {
                        // Part 2
                        val humnResult = monkeysAgain.findHuman(
                            0, Operation.Evaluation(root.operandA, "-", root.operandB)
                        )
                        println("Result of $humnResult")
                    }
                } ms"
            )

        }
    }

    fun findHuman(target: Long, operation: Operation): Long {
        return when (operation) {
            is Operation.MyInput -> target
            is Operation.Evaluation -> {
                val a = map[operation.operandA]!!
                val b = map[operation.operandB]!!
                when (operation.operator) {
                    "+" -> {
                        try {
                            findHuman(target - evaluate(a), b)
                        } catch (e: IllegalStateException) {
                            findHuman(target - evaluate(b), a)
                        }
                    }

                    "-" -> try {
                        findHuman(evaluate(a) - target, b)
                    } catch (e: IllegalStateException) {
                        findHuman(evaluate(b) + target, a)
                    }

                    "*" -> try {
                        findHuman(target / evaluate(a), b)
                    } catch (e: IllegalStateException) {
                        findHuman(target / evaluate(b), a)
                    }

                    else -> try {
                        findHuman(evaluate(a) / target, b)
                    } catch (e: IllegalStateException) {
                        findHuman(target * evaluate(b), a)
                    }
                }
            }
            else -> 0
        }
    }

    private fun evaluate(operation: Operation): Long {
        if (operation == Operation.MyInput) {
            throw IllegalStateException()
        }
        return if (operation is Operation.Number) operation.value
        else {
            val evaluation = operation as Operation.Evaluation
            when (evaluation.operator) {
                "+" -> evaluate(map[evaluation.operandA]!!) + evaluate(map[evaluation.operandB]!!)
                "-" -> evaluate(map[evaluation.operandA]!!) - evaluate(map[evaluation.operandB]!!)
                "*" -> evaluate(map[evaluation.operandA]!!) * evaluate(map[evaluation.operandB]!!)
                else -> evaluate(map[evaluation.operandA]!!) / evaluate(map[evaluation.operandB]!!)
            }
        }
    }
}

sealed interface Operation {
    companion object {
        fun parse(input: String): Operation {
            return input.toLongOrNull()?.let { Number(it) } ?: run {
                val split = input.split(" ")
                Evaluation(split[0], split[1], split[2])
            }
        }
    }

    class Number(val value: Long) : Operation {
        override fun toString(): String {
            return "$value"
        }
    }

    class Evaluation(val operandA: String, val operator: String, val operandB: String) : Operation {
        override fun toString(): String {
            return "[$operandA $operator $operandB]"
        }
    }

    object MyInput : Operation
}