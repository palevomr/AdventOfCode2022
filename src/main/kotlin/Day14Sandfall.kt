import java.io.File

class Day14Sandfall {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val sandfall = Day14Sandfall()
            val file = File("./src/main/resources/input_day_14")
            file.forEachLine {
                sandfall.addPath(it.split(" -> ").map {
                    val coordinates = it.split(",")
                    Coordinate(coordinates[0].toInt(), coordinates[1].toInt())
                })
            }
            sandfall.startSandfall()
        }
    }

    data class Coordinate(var x: Int, var y: Int)

    private val map = Array(200) {
        BooleanArray(1000)
    }
    private var voidY = 0

    fun addPath(coordinates: List<Coordinate>) {
        var prev: Coordinate? = null
        coordinates.forEach { current ->
            prev?.let { previous ->
                if (previous.x == current.x) {
                    for (y in Math.min(previous.y, current.y)..Math.max(previous.y, current.y)) {
                        map[y][current.x] = true
                    }
                } else if (previous.y == current.y) {
                    for (x in Math.min(previous.x, current.x)..Math.max(previous.x, current.x)) {
                        map[current.y][x] = true
                    }
                }
            } ?: run {
                map[current.y][current.x] = true
            }
            voidY = Math.max(voidY, current.y)
            prev = current
        }
    }

    fun startSandfall() {
        var counter = 0
        while (true) {
            if (moveSandUnit(500, 0)) {
                // some sand unit reached to max y
                break
            } else {
                counter++
            }
        }
        println("Counter - $counter")
    }

    private fun moveSandUnit(unitX: Int, unitY: Int): Boolean {
        if (unitY == voidY) {
            return true
        }
        return if (!map[unitY + 1][unitX]) {
            moveSandUnit(unitX, unitY + 1)
        } else if (!map[unitY + 1][unitX - 1]) {
            moveSandUnit(unitX - 1, unitY + 1)
        } else if (!map[unitY + 1][unitX + 1]) {
            moveSandUnit(unitX + 1, unitY + 1)
        } else {
            map[unitY][unitX] = true
            false
        }

    }
}
