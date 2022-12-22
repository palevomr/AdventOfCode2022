import java.io.File

class MonkeyMap(private val rows: Int, private val cols: Int) {
    val map = Array(rows) { Array(cols) { ' ' } }

    // cubeSize
    val size = cols / 3

    // stores first and last item on row
    private val rowsMap = mutableMapOf<Int, Pair<Item, Item>>()

    // stores first and last item on row
    private val colsMap = mutableMapOf<Int, Pair<Item, Item>>()

    //
    private var currentRow = -1
    private var currentCol = -1
    private var rotations = arrayOf(
        0 to 1, // 0
        1 to 0, // 1
        0 to -1, // 2
        -1 to 0 // 3
    )
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
            var nextRow = currentRow + rotations[currentRotationIndex].first
            var nextCol = currentCol + rotations[currentRotationIndex].second
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
                var newRotation = -1
                val side = sideOf(currentRow, currentCol)
                if (side == "1" && currentRotationIndex == 3) {
                    newRotation = 0
                    nextCol = 0
                    nextRow = 3 * size + currentCol - size // nextSide = F
                } else if (side == "1" && currentRotationIndex == 2) {
                    newRotation = 0
                    nextCol = 0
                    nextRow = 2 * size + (size - currentRow - 1) // nextSide = E
                } else if (side == "2" && currentRotationIndex == 3) {
                    newRotation = 3
                    nextCol = currentCol - 2 * size
                    nextRow = 4 * size - 1 // nextSide = F
                } else if (side == "2" && currentRotationIndex == 0) {
                    newRotation = 2
                    nextCol = 2 * size - 1
                    nextRow = (size - currentRow) + 2 * size - 1 // nextSide = D
                } else if (side == "2" && currentRotationIndex == 1) {
                    newRotation = 2
                    nextCol = 2 * size - 1
                    nextRow = size + (currentCol - 2 * size) // nextSide = C
                } else if (side == "3" && currentRotationIndex == 0) {
                    newRotation = 3
                    nextCol = (currentRow - size) + 2 * size
                    nextRow = size - 1
                } else if (side == "3" && currentRotationIndex == 2) {
                    newRotation = 1
                    nextCol = currentRow - size
                    nextRow = 2 * size
                } else if (side == "4" && currentRotationIndex == 1) {
                    newRotation = 2
                    nextCol = size - 1
                    nextRow = 3 * size + (currentCol - size)
                } else if (side == "4" && currentRotationIndex == 0) {
                    newRotation = 2
                    nextCol = 3 * size - 1
                    nextRow = size - (currentRow - size * 2) - 1
                } else if (side == "5" && currentRotationIndex == 2) {
                    newRotation = 0
                    nextCol = size
                    nextRow = size - (currentRow - 2 * size) - 1 // nextSide = A
                } else if (side == "5" && currentRotationIndex == 3) {
                    newRotation = 0
                    nextCol = size
                    nextRow = size + currentCol // nextSide = C
                } else if (side == "6" && currentRotationIndex == 0) {
                    newRotation = 3
                    nextCol = (currentRow - 3 * size) + size
                    nextRow = 3 * size - 1
                } else if (side == "6" && currentRotationIndex == 2) {
                    newRotation = 1
                    nextCol = size + (currentRow - 3 * size)
                    nextRow = 0 // nextSide = A
                } else if (side == "6" && currentRotationIndex == 1) {
                    newRotation = 1
                    nextCol = currentCol + 2 * size
                    nextRow = 0
                }
                if (map[nextRow][nextCol] == '#') break
                else {
                    currentRow = nextRow
                    currentCol = nextCol
                    currentRotationIndex = newRotation
                    current++
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

    private fun sideOf(row: Int, column: Int): String {
        if (column in size until 2 * size && row in 0 until size) return "1"
        if (column in 2 * size until 3 * size && row in 0 until size) return "2"
        if (column in size until 2 * size && row in size until 2 * size) return "3"
        if (column in size until 2 * size && row in 2 * size until 3 * size) return "4"
        if (column in 0 until size && row in 2 * size until 3 * size) return "5"
        if (column in 0 until size && row in 3 * size until 4 * size) return "6"
        return ""
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
