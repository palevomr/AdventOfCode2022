import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

class Day18LavaFall(
    val minX: Int, val maxX: Int,
    val minY: Int, val maxY: Int,
    val minZ: Int, val maxZ: Int,
    val occupied: HashSet<Coordinates3D>
) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_18")
            val regexNumber = Regex("\\d+")
            var maxX = Int.MIN_VALUE
            var maxY = Int.MIN_VALUE
            var maxZ = Int.MIN_VALUE
            var minX = Int.MAX_VALUE
            var minY = Int.MAX_VALUE
            var minZ = Int.MAX_VALUE
            val lavaDrops = file.readLines()
                .map {
                    regexNumber.findAll(it).map { it.value.toInt() }.iterator()
                }.map {
                    val x = it.next()
                    minX = minOf(x, minX)
                    maxX = maxOf(x, maxX)
                    val y = it.next()
                    maxY = maxOf(y, maxY)
                    minY = minOf(y, minY)
                    val z = it.next()
                    maxZ = maxOf(z, maxZ)
                    minZ = minOf(z, minZ)
                    Coordinates3D(x, y, z)
                }.toHashSet()

            val lavaFall = Day18LavaFall(
                minX, maxX, minY, maxY, minZ, maxZ, lavaDrops
            )
            println(
                "${
                    measureTimeMillis {
                        println(lavaFall.calculateVisible())
                    }
                } ms"
            )
        }
    }


    fun calculateVisible(): Int {
        val checked = mutableSetOf<Coordinates3D>()
        val stack = Stack<Coordinates3D>()
        stack.push(Coordinates3D(minX - 1, minY - 1, minZ - 1))
        var visibleSurfaceMinusTrapped = 0
        while (stack.isNotEmpty()) {
            val item = stack.pop()
            if (checked.contains(item)) continue
            checked.add(item)
            if (item.x in minX - 1..maxX + 1 &&
                item.y in minY - 1..maxY + 1 &&
                item.z in minZ - 1..maxZ + 1
            ) item.findNeighbors()
                .forEach {
                    if (occupied.contains(it)) visibleSurfaceMinusTrapped++
                    else {
                        if (!checked.contains(it)) stack.push(it)
                    }
                }

        }
        return visibleSurfaceMinusTrapped
    }
}

data class Coordinates3D(val x: Int, val y: Int, val z: Int)

fun Coordinates3D.findNeighbors(): List<Coordinates3D> {
    return listOf(
        Coordinates3D(x - 1, y, z),
        Coordinates3D(x + 1, y, z),
        Coordinates3D(x, y + 1, z),
        Coordinates3D(x, y - 1, z),
        Coordinates3D(x, y, z - 1),
        Coordinates3D(x, y, z + 1),
    )
}
