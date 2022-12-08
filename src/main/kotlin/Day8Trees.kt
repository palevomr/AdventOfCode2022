import java.io.File

class Day8Trees {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_8")
            val grid = generateGrid(file.readLines())

            var maxScore = 0
            fun computeScoreDirection(row: Int, col: Int, xDir: Int, yDir: Int): Int {
                var currentRow: Int = row + yDir
                var currentCol: Int = col + xDir
                var countTrees = 0
                while (currentRow >= 0 && currentRow <= grid.size - 1 && currentCol >= 0 && currentCol <= grid[0].size - 1) {
                    countTrees++
                    if (grid[currentRow][currentCol] >= grid[row][col]) {
                        break
                    }
                    currentRow += yDir
                    currentCol += xDir
                }

                return countTrees
            }

            for (i in 1 until grid.size - 1) {
                for (j in 1 until grid[0].size - 1) {
                    maxScore = Math.max(
                        maxScore,
                        computeScoreDirection(i, j, 0, 1) *
                                computeScoreDirection(i, j, 0, -1) *
                                computeScoreDirection(i, j, 1, 0) *
                                computeScoreDirection(i, j, -1, 0)
                    )
                }
            }
            println(maxScore)
        }

        private fun generateGrid(stringGrid: List<String>): Array<IntArray> {
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