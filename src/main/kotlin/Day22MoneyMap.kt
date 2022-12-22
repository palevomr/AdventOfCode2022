import java.io.File

class MonkeyMap(val rows: Int, val cols: Int) {
    val map = Array(rows) {
        Array(cols) { ' ' }
    }

    // stores first and last item on row
    private val rowsMap = mutableMapOf<Int, Pair<Item, Item>>()

    // stores first and last item on row
    private val colsMap = mutableMapOf<Int, Pair<Item, Item>>()

    //
    private var currentRow = -1
    private var currentCol = -1
    private var rotations = arrayOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
    private var currentRotationIndex = 0

    fun processInput(input: List<String>) {
        input.forEachIndexed { rowIdx, row ->
            var firstItem: Item? = null
            var lastItem: Item? = null
            row.forEachIndexed { index, c ->
                if (c != ' ') {
                    map[rowIdx][index] = c
                    if (firstItem == null) firstItem = Item(c, index)
                    lastItem = Item(c, index)
                }
            }
            rowsMap[rowIdx] = firstItem!! to lastItem!!
        }

        repeat(cols) { column ->
            var firstItem: Item? = null
            var lastItem: Item? = null
            repeat(rows) { row ->
                val current = map[row][column]
                if (current != ' ') {
                    if (firstItem == null) firstItem = Item(current, row)
                    lastItem = Item(current, row)
                }
            }
            colsMap[column] = firstItem!! to lastItem!!
        }

        currentCol = rowsMap[0]!!.first.index
        currentRow = 0
    }

    private fun startSimulation(moveInput: String) {
        val regex = Regex("\\d+|[A-Z]")
        println("Starting at $currentRow $currentCol")
        regex.findAll(moveInput).map { it.value }.forEach {
            val moves = it.toIntOrNull()
            if (moves == null) {
                rotate(it)
            } else {
                move(moves)
            }
        }
        println("Result is ${1000 * (currentRow + 1) + 4 * (currentCol + 1) + currentRotationIndex}")
    }

    private fun move(moves: Int) {
        var current = 0
        while (current < moves) {
            val nextRow = currentRow + rotations[currentRotationIndex].first
            val nextCol = currentCol + rotations[currentRotationIndex].second
            if (nextRow in 0 until rows && nextCol in 0 until cols && map[nextRow][nextCol] != ' ') {
                if (map[nextRow][nextCol] == '.') {
                    currentRow = nextRow
                    currentCol = nextCol
                    current++
                } else if (map[nextRow][nextCol] == '#') { // hit the wall
                    break
                }
            } else {
                // wrap around
                when (currentRotationIndex) {
                    // right
                    0 -> {
                        val nextItem = rowsMap[nextRow]!!.first
                        if (nextItem.tile != '#') {
                            currentCol = nextItem.index
                            currentRow = nextRow
                            current++
                        } else break
                    }
                    // bottom
                    1 -> {
                        val nextItem = colsMap[nextCol]!!.first
                        if (nextItem.tile != '#') {
                            currentRow = nextItem.index
                            currentCol = nextCol
                            current++
                        } else break
                    }
                    // left
                    2 -> {
                        val nextItem = rowsMap[nextRow]!!.second
                        if (nextItem.tile != '#') {
                            currentCol = nextItem.index
                            currentRow = nextRow
                            current++
                        } else break
                    }
                    // top
                    3 -> {
                        val nextItem = colsMap[nextCol]!!.second
                        if (nextItem.tile != '#') {
                            currentRow = nextItem.index
                            currentCol = nextCol
                            current++
                        } else break
                    }
                }
            }
        }
    }

    private fun rotate(rotation: String) {
        if (rotation == "R") {
            currentRotationIndex = (currentRotationIndex + 1) % 4
        } else {
            currentRotationIndex = if (currentRotationIndex == 0) 3 else (currentRotationIndex - 1)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_22")
            val input = file.readLines()
            val mapInput = input.dropLast(2)
            val monkeyMap = MonkeyMap(mapInput.size, mapInput.maxOf { it.length })
            monkeyMap.processInput(mapInput)
            monkeyMap.startSimulation(input.last())
        }
    }
}

data class Item(val tile: Char, val index: Int)
