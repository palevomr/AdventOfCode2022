import java.io.File

class Day17FallingRocks(
    val jetPattern: List<Int>,
) {
    companion object {

        private const val WIDTH = 7
        private const val HEIGHT = 20_000
        private const val BOTTOM = WIDTH * HEIGHT

        private val figures = listOf(
            Figure(intArrayOf(0, 1, 2, 3)),
            Figure(intArrayOf(1, 1 - WIDTH, 1 - 2 * WIDTH, 2 - WIDTH, -WIDTH)),
            Figure(intArrayOf(0, 1, 2, 2 - WIDTH, 2 - 2 * WIDTH)),
            Figure(intArrayOf(0, -WIDTH, -2 * WIDTH, -3 * WIDTH)),
            Figure(intArrayOf(0, 1, -WIDTH, -WIDTH + 1)),
        )

        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_17")
            val day17FallingRocks = Day17FallingRocks(
                jetPattern = file.readText().map { if (it == '<') -1 else +1 })
            day17FallingRocks.simulate(1000000000000L)
            println(day17FallingRocks.getCurrentHeight())
        }
    }


    private val taken = BooleanArray((HEIGHT + 1) * WIDTH)
    private var figureCount = 0L
    private var patternCount = 0
    private var currentRow = HEIGHT

    fun simulate(target: Long) {
        while (figureCount < target) {
            dropRock()
        }
    }
    private fun dropRock() {
        val figure = figures[(figureCount++ % figures.size).toInt()].applySpawningPoint(currentRow)
        while (true) {
            val pattern = jetPattern[patternCount % jetPattern.size].also { patternCount++ }
            if (pattern < 0) figure.moveLeft(taken)
            else if (pattern > 0) figure.moveRight(taken)
            if (figure.canFall(taken)) figure.fall()
            else {
                // settle down
                figure.coordinates.forEach {
                    taken[it] = true
                }
                currentRow = minOf(currentRow, figure.coordinates.min() / WIDTH)
                break
            }
        }
    }

    fun getCurrentHeight(): Int {
        return HEIGHT - currentRow
    }

    class Figure(val coordinates: IntArray) {
        fun applySpawningPoint(currentRow: Int): Figure {
            val coordinate = currentRow * WIDTH
            return Figure(coordinates.map { it + (coordinate / WIDTH - 4) * WIDTH + 2 }.toIntArray())
        }

        fun moveLeft(taken: BooleanArray) {
            if (coordinates.any { it % WIDTH == 0 || taken[it - 1] }) return
            for (i in coordinates.indices) coordinates[i]--
        }

        fun moveRight(taken: BooleanArray) {
            if (coordinates.any { it % WIDTH == WIDTH - 1 || taken[it + 1] }) return
            for (i in coordinates.indices) coordinates[i]++
        }

        fun canFall(taken: BooleanArray): Boolean {
            return coordinates.none { it + WIDTH > BOTTOM || taken[it + WIDTH] }
        }

        fun fall() {
            for (i in coordinates.indices) coordinates[i] += WIDTH
        }
    }
}

