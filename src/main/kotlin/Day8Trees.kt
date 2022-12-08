import java.io.File

class Day8Trees {

    data class GridData(
        var maxVert: Int,
        var maxHor: Int,
        var counted: Boolean = false
    )

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_8")
            val grid = generateGrid(file.readLines())

            val maxHeightGrid = Array(grid.size) {
                Array(grid[0].size) { GridData(-1, -1) }
            }

            for (i in grid.indices) maxHeightGrid[i][0].maxHor = grid[i][0]
            for (i in grid.indices) maxHeightGrid[i][grid.size - 1].maxHor = grid[i][grid.size - 1]
            for (i in grid[0].indices) maxHeightGrid[0][i].maxVert = grid[0][i]
            for (i in grid[0].indices) maxHeightGrid[grid[0].size - 1][i].maxVert = grid[grid[0].size - 1][i]
            var visible = grid.size * 2 + (grid[0].size - 2) * 2
            for (i in 1 until grid.size - 1) {
                for (j in 1 until grid[0].size - 1) {
                    val value = grid[i][j]
                    val horMax = maxHeightGrid[i][j - 1].maxHor
                    val vertMax = maxHeightGrid[i - 1][j].maxVert
                    if ((value > vertMax || value > horMax)) {
                        visible++
                        maxHeightGrid[i][j].counted = true
                    }
                    maxHeightGrid[i][j].maxHor = Math.max(horMax, value)
                    maxHeightGrid[i][j].maxVert = Math.max(vertMax, value)
                }
            }
            for (i in grid.size - 2 downTo 1) {
                for (j in grid[0].size - 2 downTo 1) {
                    val value = grid[i][j]
                    val horMax = maxHeightGrid[i][j + 1].maxHor
                    val vertMax = maxHeightGrid[i + 1][j].maxVert
                    if (!maxHeightGrid[i][j].counted && (value > vertMax || value > horMax)) {
                        visible++
                        maxHeightGrid[i][j].counted = true
                    }
                    maxHeightGrid[i][j].maxHor = Math.max(horMax, value)
                    maxHeightGrid[i][j].maxVert = Math.max(vertMax, value)
                }
            }
            println("Visible - $visible")
        }

        fun generateGrid(stringGrid: List<String>): Array<IntArray> {
            val grid = Array(stringGrid.size) {
                IntArray(stringGrid[0].length)
            }
            stringGrid.forEachIndexed { i, s ->
                s.forEachIndexed { j, c ->
                    grid[i][j] = c.digitToInt()
                }
            }
            return grid
        }
    }
}