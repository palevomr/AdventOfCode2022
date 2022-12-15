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
            sandfall.fillFloor()
            sandfall.startSandfall()
        }
    }

    private val map = Array(200) {
        BooleanArray(1000)
    }
    private var floor = 0

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
            floor = Math.max(floor, current.y)
            prev = current
        }

    }

    fun fillFloor() {
        floor += 2
        println(floor)
        for (i in 0 until map[0].size) {
            map[floor][i] = true
        }
    }

    fun startSandfall() {
        var counter = 0L
        while (true) {
            if (map[0][500]) {
                break
            }
            moveSandUnit(500, 0)
            counter++
        }
        map.forEach {
            println(it.map {
                if (it) "#" else "."
            }.joinToString(""))
        }
        println("Counter - $counter")
    }

    private fun moveSandUnit(unitX: Int, unitY: Int) {
        if (!map[unitY + 1][unitX]) {
            moveSandUnit(unitX, unitY + 1)
        } else if (!map[unitY + 1][unitX - 1]) {
            moveSandUnit(unitX - 1, unitY + 1)
        } else if (!map[unitY + 1][unitX + 1]) {
            moveSandUnit(unitX + 1, unitY + 1)
        } else {
            map[unitY][unitX] = true
        }

    }
}
