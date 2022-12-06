import java.io.File

class Day6TuningTrouble {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_6")
            val input = file.readText()

            val map = mutableMapOf<Char, Int>()
            var i = 0
            while (i < input.length) {
                map[input[i]] = (map[input[i]] ?: 0) + 1
                val prevItem = i - 14
                if (prevItem >= 0) {
                    map[input[prevItem]] = (map[input[prevItem]] ?: 0) - 1
                    if (map[input[prevItem]] == 0) map.remove(input[prevItem])
                }
                if (map.size == 14) {
                    println("Char -  ${i + 1}")
                    break
                }
                i++
            }
        }
    }
}
