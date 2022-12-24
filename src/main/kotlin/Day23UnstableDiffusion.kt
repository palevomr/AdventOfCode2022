import java.io.File

class UnstableDiffusion {
    companion object {

        val DIRECTIONS = arrayOf(
            arrayOf(Coordinate(-1, -1), Coordinate(-1, 0), Coordinate(-1, 1)),
            arrayOf(Coordinate(1, -1), Coordinate(1, 0), Coordinate(1, 1)),
            arrayOf(Coordinate(-1, -1), Coordinate(0, -1), Coordinate(1, -1)),
            arrayOf(Coordinate(-1, 1), Coordinate(0, 1), Coordinate(1, 1)),
        )
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_23")
            val input = file.readLines()
            val unstableDiffusion = UnstableDiffusion()
            input.forEachIndexed { x, string ->
                string.forEachIndexed { y, value ->
                    if (value == '#') {
                        unstableDiffusion.addElf(x, y)
                    }
                }
            }
            unstableDiffusion.simulate(10)
            unstableDiffusion.printMap(unstableDiffusion.set)
        }
    }

    private var set = mutableSetOf<Coordinate>()

    fun addElf(row: Int, col: Int) {
        set.add(Coordinate(row, col))
    }

    fun simulate(times: Int) {
        var directions = DIRECTIONS.toList()
        for (i in 0 until times) {
            val newSet = move(directions)
            if (set == newSet) break
            set = newSet
            directions = directions.takeLast(3) + directions.take(1)
        }

        val rowMin = set.minOf { it.x }
        val rowMax = set.maxOf { it.x }
        val colMin = set.minOf { it.y }
        val colMax = set.maxOf { it.y }
        val space = (rowMax + 1 - rowMin) * (colMax + 1 - colMin)
        println("Free area = ${space - set.size}")

    }

    fun printMap(set: Set<Coordinate>) {
        val rowMin = set.minOf { it.x }
        val rowMax = set.maxOf { it.x }
        val colMin = set.minOf { it.y }
        val colMax = set.maxOf { it.y }
        for (row in rowMin - 1..rowMax + 2) {
            for (col in colMin - 2..colMax + 3) {
                if (set.contains(Coordinate(row, col))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun move(directions: List<Array<Coordinate>>): MutableSet<Coordinate> {
        val map = mutableMapOf<Coordinate, Int>()
        set.forEach { current ->
            val neighbourGroups = directions.map {
                it.map { Coordinate(current.x + it.x, current.y + it.y) }
            }
            if (neighbourGroups.flatten().any { set.contains(it) }) {
                for (neighbours in neighbourGroups) {
                    if (neighbours.none { set.contains(it) }) {
                        map[neighbours[1]] = map.getOrDefault(neighbours[1], 0) + 1
                        break
                    }
                }
            }
        }

        return set.map { current ->
            val neighbourGroups = directions.map {
                it.map { Coordinate(current.x + it.x, current.y + it.y) }
            }
            if (neighbourGroups.flatten().any { set.contains(it) }) {
                for (direction in directions) {
                    val neighbours = direction.map { Coordinate(current.x + it.x, current.y + it.y) }
                    if (neighbours.none { set.contains(it) }) {
                        if (map[neighbours[1]] == 1) {
                            return@map neighbours[1]
                        }
                        break
                    }
                }
            }
            return@map current
        }.toMutableSet()
    }

}