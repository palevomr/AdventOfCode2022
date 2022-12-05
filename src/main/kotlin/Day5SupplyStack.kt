import java.io.File
import java.util.*

class Day5SupplyStack {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("/Users/edwardfedorow/IdeaProjects/AdventOfCode/src/main/resources/input_day_5")

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
        }

    }
}