import java.io.File

class Day3RucksackProblem {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_3")
            var typeSum = 0
            var counter = 0
            var typeMap = Array(2) {
                BooleanArray(64)
            }
            file.forEachLine {
                println("$it, $counter")
                if (counter == 0 || counter == 1) {
                    for (element in it) {
                        typeMap[counter][element - 'A'] = true
                    }
                    counter++
                } else if (counter == 2) {
                    for (element in it) {
                        val value = element - 'A'
                        if (typeMap[0][value] && typeMap[1][value]) {
                            typeSum += if (value > 32) {
                                value - 31
                            } else {
                                value + 27
                            }
                            println("$element - += $value $typeSum")
                            break
                        }
                    }
                    typeMap = Array(2) { BooleanArray(64) }
                    counter = 0
                }
            }
        }
    }
}