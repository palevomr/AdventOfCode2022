import java.io.File
import kotlin.system.measureTimeMillis

class BlizzardBasin(private val rows: Int, private val cols: Int) {

    private val startRow = 0
    private val startCol = 1

    private val field = Array(rows) {
        CharArray(cols) { '#' }
    }
    private val set = mutableSetOf<Blizzard>()

    fun add(row: Int, col: Int, value: Char) {
        when (value) {
            '^' -> set.add(Blizzard(row, col, DIRECTIONS[0]))
            '<' -> set.add(Blizzard(row, col, DIRECTIONS[1]))
            'v' -> set.add(Blizzard(row, col, DIRECTIONS[2]))
            '>' -> set.add(Blizzard(row, col, DIRECTIONS[3]))
        }
        field[row][col] = value
    }

    fun visualize() {
        field.forEach { println(it.concatToString()) }
    }

    fun part2(): Int {
        val forward = simulate(startRow, startCol, rows - 1, cols - 2)
        println("Way forward $forward")
        val back = simulate(rows - 1, cols - 2, startRow, startCol)
        println("Way back $back")
        val forwardAgain = simulate(startRow, startCol, rows - 1, cols - 2)
        println("Way forward again $forwardAgain")
        return forward + back + forwardAgain
    }

    fun simulate(startRow: Int, startCol: Int, endRow: Int, endCol: Int): Int {
        val possiblePositions = mutableListOf(MyState(startRow, startCol))
        var moves = 0
        while (true) {
            val newPossiblePositions = mutableSetOf<MyState>()
            for (current in possiblePositions) {
                for (directon in DIRECTIONS) {
                    val tempRow = current.row + directon.x
                    val tempCol = current.col + directon.y
                    if (tempRow in 0 until rows && tempCol in 0 until cols) {
                        newPossiblePositions.add(MyState(tempRow, tempCol))
                    }
                }

            }
            moveBlizzards()
            possiblePositions.clear()
            for (position in newPossiblePositions) {
                if (position.row == endRow && position.col == endCol) {
                    return moves + 1
                } else if (field[position.row][position.col] == '.') {
                    possiblePositions.add(position)
                }
            }
            moves++
        }
    }

    fun moveBlizzards() {
        set.forEach {
            var nextCol = it.col + it.direction.y
            var nextRow = it.row + it.direction.x
            if (nextCol == 0) nextCol = cols - 2
            if (nextCol == cols - 1) nextCol = 1
            if (nextRow == 0) nextRow = rows - 2
            if (nextRow == rows - 1) nextRow = 1
            it.col = nextCol
            it.row = nextRow
        }
        for (i in 1 until field.size - 1) {
            for (j in 1 until field[0].size - 1) {
                field[i][j] = '.'
            }
        }

        set.forEach {
            if (field[it.row][it.col] == '.') {
                field[it.row][it.col] = it.direction.toDirection()
            } else {
                field[it.row][it.col] = '@'
            }
        }
    }

    companion object {

        val DIRECTIONS = arrayOf(
            Coordinate(-1, 0), // top
            Coordinate(0, -1), // left
            Coordinate(1, 0), // bottom
            Coordinate(0, 1), // right
            Coordinate(0, 0) // stale
        )

        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_24")
            val input = file.readLines()
            val blizzardBasin = BlizzardBasin(input.size, input[0].length)
            input.forEachIndexed { row, line ->
                line.forEachIndexed { col, value ->
                    blizzardBasin.add(row, col, value)
                }
            }
            println(
                "${
                    measureTimeMillis {
                        println("Answer part 2 is ${blizzardBasin.part2()}")
                    }
                } ms"
            )

        }
    }

    private fun Coordinate.toDirection(): Char {
        return if (this == DIRECTIONS[0]) '^' else if (this == DIRECTIONS[1]) '<' else if (this == DIRECTIONS[2]) 'v' else '>'
    }
}

data class MyState(val row: Int, val col: Int)
data class Blizzard(var row: Int, var col: Int, val direction: Coordinate)