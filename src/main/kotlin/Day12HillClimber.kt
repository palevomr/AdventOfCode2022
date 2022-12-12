import java.io.File
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

class Day12HillClimber {

    private val startPosition: Coordinates = Coordinates(-1, -1)
    private val endPosition: Coordinates = Coordinates(-1, -1)
    private val field = mutableListOf<MutableList<Char>>()

    fun addRow(row: String) {
        field.add(row.mapIndexed { idx, char ->
            when (char) {
                'S' -> {
                    startPosition.row = field.size
                    startPosition.column = idx
                    'a'
                }

                'E' -> {
                    endPosition.row = field.size
                    endPosition.column = idx
                    'z'
                }

                else -> char
            }

        }.toMutableList())
    }

    private val directions = arrayOf(
        0 to 1, 0 to -1, -1 to 0, 1 to 0
    )

    fun findShortestDistance(): Int {
        println("S - $startPosition E - $endPosition")
        val visited = Array(field.size) {
            BooleanArray(field[0].size)
        }
        val queue = PriorityQueue<Coordinates> { a, b -> a.steps - b.steps}
        queue.offer(endPosition)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (visited[current.row][current.column]) continue
            visited[current.row][current.column] = true

            if (field[current.row][current.column] == 'a') {
                println(current.steps)
                return current.steps
            }

            directions.forEach {
                val newRow = current.row + it.first
                val newCol = current.column + it.second
                if (newRow in 0 until field.size && newCol in 0 until field[0].size
                    && (field[current.row][current.column] - field[newRow][newCol] <= 1)
                ) {
                    queue.offer(Coordinates(newRow, newCol, current.steps + 1))
                }
            }
        }
        return -1
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_12")
            val hillClimber = Day12HillClimber()
            file.forEachLine {
                hillClimber.addRow(it)
            }
            hillClimber.findShortestDistance()
        }
    }

    data class Coordinates(
        var row: Int,
        var column: Int,
        var steps: Int = 0
    )
}