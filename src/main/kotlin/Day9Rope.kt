import java.io.File

/**
 *  4 ..##..
 *  3 ...##.
 *  2 .####.
 *  1 ....#.
 *  0 s###..
 *    012345
 */
class Day9Rope {
    data class Coordinates(var x:Int, var y: Int) {
        override fun toString(): String {
            return "($x $y)"
        }
    }
    private var rope = Array(10) {
        Coordinates(0, 0)
    }

    private val visitedPositionsByTailSet = mutableSetOf<Pair<Int, Int>>()
    val visitedPositionsByTail: Int
        get() = visitedPositionsByTailSet.size

    fun moveDown(n: Int) {
        repeat(n) {
            rope[0].y--
            adjustPositions()
        }
    }

    fun moveUp(n: Int) {
        repeat(n) {
            rope[0].y++
            adjustPositions()
        }
    }

    fun moveLeft(n: Int) {
        repeat(n) {
            rope[0].x--
            adjustPositions()
        }

    }

    fun moveRight(n: Int) {
        repeat(n) {
            rope[0].x++
            adjustPositions()
        }
    }

    private fun adjustPositions() {
        for (i in 0 until rope.size - 1) {
            adjustTail(rope[i], rope[i + 1])
        }
        println(rope.toList())
        visitedPositionsByTailSet.add(rope.last().x to rope.last().y)
    }
    private fun adjustTail(coordinateHead: Coordinates, coordinateTail: Coordinates) {
        val xDiff = coordinateHead.x - coordinateTail.x
        val yDiff = coordinateHead.y - coordinateTail.y
        val xDiffAbs = Math.abs(xDiff)
        val yDiffAbs = Math.abs(yDiff)

        if (yDiff == 0 && xDiff != 0) {
            coordinateTail.x += if (xDiff > 1) 1 else if (xDiff < -1) -1 else 0
        } else if (xDiff == 0 && yDiff != 0) {
            coordinateTail.y += if (yDiff > 1) 1 else if (yDiff < -1) -1 else 0
        } else if (xDiffAbs == 1 && yDiffAbs == 2) {
            coordinateTail.x = coordinateHead.x
            coordinateTail.y += if (yDiff > 1) 1 else if (yDiff < -1) -1 else 0
        } else if (yDiffAbs == 1 && xDiffAbs == 2) {
            coordinateTail.y = coordinateHead.y
            coordinateTail.x += if (xDiff > 1) 1 else if (xDiff < -1) -1 else 0
        } else if (yDiffAbs == 2 && xDiffAbs == 2) {
            coordinateTail.y += if (yDiff > 1) 1 else if (yDiff < -1) -1 else 0
            coordinateTail.x += if (xDiff > 1) 1 else if (xDiff < -1) -1 else 0
        }
    }
}

object Day9RopeRun {
    @JvmStatic
    fun main(args: Array<String>) {
        val file = File("./src/main/resources/input_day_9")
        val rope = Day9Rope()
        file.forEachLine {
            println("$it")
            it.split(" ").let {
                val direction = it[0]
                val count = it[1].toInt()
                when (direction) {
                    "D" -> rope.moveDown(count)
                    "U" -> rope.moveUp(count)
                    "L" -> rope.moveLeft(count)
                    "R" -> rope.moveRight(count)
                }
            }
        }
        println(rope.visitedPositionsByTail)
    }
}