object Day2RockScissors {

    // 1 - rock. 2 - scissors. 3 - paper
    fun calculateScore1(input: List<Pair<GameVariant, GameVariant>>): Int {
        var sum = 0
        input.forEach {
            val hisChoice = it.first
            val myChoice = it.second

            sum += myChoice.value + when {
                hisChoice == myChoice -> 3
                hisChoice == GameVariant.ROCK && myChoice == GameVariant.SCISSORS-> 0
                hisChoice == GameVariant.PAPER && myChoice == GameVariant.ROCK -> 0
                hisChoice == GameVariant.SCISSORS && myChoice == GameVariant.PAPER -> 0
                else -> 6
            }
        }
        return sum
    }

    fun calculateScore2(input: List<Pair<GameVariant, Int>>): Int {
        var sum = 0
        input.forEach {
            val hisChoice = it.first
            val gameOutcome = it.second
            sum += when (gameOutcome) {
                0 -> { // lose
                    0 + (hisChoice.value - 1).let { if (it == 0) 3 else it }
                }
                1 -> { // win
                    6 + (hisChoice.value + 1).let { if (it == 4) 1 else it }
                }
                else -> { // draw
                    3 + hisChoice.value
                }
            }

        }
        return sum
    }
}

enum class GameVariant(val value: Int) {
    ROCK(1), PAPER(2), SCISSORS(3)
}