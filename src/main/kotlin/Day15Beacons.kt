import java.io.File
import kotlin.math.max

class Beacons {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val beacons = Beacons()
            val file = File("./src/main/resources/input_day_15")
            file.forEachLine {
                beacons.processInput(it)
            }
            beacons.coverArea(4_000_000)
        }
    }

    data class CoveredArea(
        val xRange: IntRange,
        val yRange: IntRange,
        val beacon: Coordinate,
        val sensor: Coordinate,
        val md: Int
    )

    private val coveredArea = mutableListOf<CoveredArea>()

    fun coverArea(maxRange: Int) {
        var x = 0
        var y = 0
        while (x < maxRange || y < maxRange) {
            val firstSensor = coveredArea.firstOrNull {
                it.md >= manhattanDistance(x, y, it.sensor.x, it.sensor.y)
            }
            if (firstSensor == null) {
                println("Answer at $x, $y ${x * 4000000L + y}");
                break
            }

            val mhdToCandidat = manhattanDistance(x, y, firstSensor.sensor.x, firstSensor.sensor.y)
            val mhdToBeacon = firstSensor.md

            val nextX = x + (mhdToBeacon - mhdToCandidat + 1)
            x = if (nextX > maxRange) 0 else nextX
            y = if (nextX > maxRange) y + 1 else y
        }
    }

    fun countItemsOnRow(row: Int) {
        val set = mutableSetOf<Coordinate>()
        coveredArea.forEach {
            val xRange = it.xRange
            val yRange = it.yRange
            val md = (yRange.last - yRange.first) / 2
            val centerX = (xRange.last + xRange.first) / 2
            val centerY = (yRange.last + yRange.first) / 2
            if (row in yRange) {
                xRange.forEach { col ->
                    val coordinate = Coordinate(col, row)
                    if (!set.contains(coordinate)
                        && coordinate != it.beacon
                        && manhattanDistance(col, row, centerX, centerY) <= md
                    ) {
                        set.add(coordinate)
                    }
                }
            }
        }
        println("Covered items for $row = ${set.size}")
    }

    //Sensor at x=3842919, y=126080: closest beacon is at x=3943893, y=1918172
    private fun processInput(input: String) {
        var i = 0
        var sensorPosition: Coordinate? = null
        var beaconPosition: Coordinate? = null
        var x: Int? = null
        var y: Int? = null
        while (i < input.length) {
            if (input[i] == 'x') {
                val xString = getInt(input, i + 2)
                i += (xString.length + 2)
                x = xString.toInt()
            } else if (input[i] == 'y') {
                val yString = getInt(input, i + 2)
                i += (yString.length + 2)
                y = yString.toInt()
            } else if (input[i] == ':') {
                x = 0
                y = 0
                i++
            } else {
                i++
            }
            if (x != null && y != null) {
                if (sensorPosition == null) {
                    sensorPosition = Coordinate(x, y)
                } else {
                    beaconPosition = Coordinate(x, y)
                }
            }
        }
        if (beaconPosition != null && sensorPosition != null) {
            val md = beaconPosition.manhattanDistance(sensorPosition) ?: 0
            coveredArea.add(
                CoveredArea(
                    (sensorPosition.x - md)..(sensorPosition.x + md),
                    (sensorPosition.y - md)..(sensorPosition.y + md),
                    beaconPosition,
                    sensorPosition,
                    md
                )
            )
        }
    }

    private fun Coordinate.manhattanDistance(coordinate: Coordinate): Int {
        return manhattanDistance(x, y, coordinate.x, coordinate.y)
    }

    private fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2)
    }

    private fun getInt(input: String, startPosition: Int): String {
        var j = startPosition
        while (j < input.length && (input[j].isDigit() || input[j] == '-')) {
            j++
        }
        return input.slice(startPosition until j)
    }
}