import java.io.File
import java.util.*

class Day5SupplyStack {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_5")

            val rows = mutableListOf<String>()
            file.forEachLine { rows.add(it) }
            val reversedRows = rows.reversed()
            val stacks = mutableListOf<Stack<Char>>()
            reversedRows[0].forEach {
                if (it == '[') stacks.add(Stack())
            }

            stacks.forEachIndexed { index, chars ->
                var row = 0
                while (row < reversedRows.size) {
                    val charIndex = 4 * index + 1
                    if (reversedRows[row][charIndex].isLetter()) {
                        chars.add(reversedRows[row][charIndex])
                        row++
                    } else {
                        break
                    }
                }
            }

            stacks.forEach {
                println(it)
            }
            val fileMoves = File("/Users/edwardfedorow/IdeaProjects/AdventOfCode/src/main/resources/input_day_5_2")
            fileMoves.forEachLine {
                var i = 0
                var itemsToMove = 0
                while (i < it.length) {
                    when {
                        it[i] == 'm' -> {
                            // poll items
                            i += 5
                            var factor = 1
                            while (it[i].isDigit()) {
                                itemsToMove = itemsToMove * factor + it[i].digitToInt()
                                i++
                                factor *= 10
                            }
                            i++
                        }

                        it[i] == 'f' -> {
                            i += 5
                            val stackSender = it[i].digitToInt()
                            i += 5
                            val stackReceiver = it[i].digitToInt()
                            val items = mutableListOf<Char>()
                            repeat(itemsToMove) {
                                items.add(stacks[stackSender - 1].pop())
                            }
                            stacks[stackReceiver - 1].addAll(items.reversed())
                            i++
                        }
                    }
                }
            }

            println("Result")
            stacks.forEach {
                println(it)
            }
            stacks.forEach {
                print(it.peek())
            }

        }
    }
}