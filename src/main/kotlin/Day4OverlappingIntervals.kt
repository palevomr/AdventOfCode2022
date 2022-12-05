import java.io.File

class Day4OverlappingIntervals {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("/Users/edwardfedorow/IdeaProjects/AdventOfCode/src/main/resources/input_day_4")

            var counter = 0
            file.forEachLine {
                val assignements = it.split(",").map { it.toInterval() }.sortedBy { it.first }
                print(assignements)

                if (assignements[0].second >= assignements[1].first) {
                    counter++
                    println(" !")
                } else {
                    println()
                }
            }
            println(counter)
        }

        fun String.toInterval(): Pair<Int, Int> {
            return split("-").let {
                it[0].toInt() to it[1].toInt()
            }
        }
    }
}