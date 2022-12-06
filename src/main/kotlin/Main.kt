import java.io.File

fun main(args: Array<String>) {
    processDay2()
}

fun processDay2() {
    val file = File("./src/main/resources/input_day_2")
    val input = mutableListOf<Pair<GameVariant, Int>>()
    file.forEachLine {
        val parts = it.split(" ")
        val hisChoice =
            if (parts[0] == "A") GameVariant.ROCK else if (parts[0] == "B") GameVariant.PAPER else GameVariant.SCISSORS
        val myChoice =
            if (parts[1] == "X") 0 else if (parts[1] == "Y") 2 else 1
        input.add(
            hisChoice to myChoice
        )
    }
    println(
        Day2RockScissors.calculateScore2(
            input
        )
    )
}

fun processDay1(args: Array<String>) {
    val file = File("./src/main/resources/input_day_1")
    val input = mutableListOf<List<Int>>()
    val current = mutableListOf<Int>()
    file.forEachLine {
        val value = it.toIntOrNull()
        if (value == null) {
            input.add(current.toList())
            current.clear()
        } else {
            current.add(value)
        }
    }
    println(Day1SumArray.finMaxTopK(input, k = 3))
}

