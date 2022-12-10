import java.io.File

class Day10CathodeRayTube {

    private var cycle = 0
    private var xValue = 1

    private val crt = Array(6) {
        CharArray(40) { '.' }
    }

    fun noop() {
        update()
        cycle++
    }

    fun addX(inc: Int) {
        noop()
        noop()
        xValue += inc
    }

    fun printCrt() {
        crt.forEach { println(it.concatToString()) }
    }

    private fun update() {
        val position = cycle % 40
        crt[cycle / 40][position] = if (position in xValue - 1..xValue + 1) {
            '#'
        } else {
            '.'
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_10")
            val cathodeRayTube = Day10CathodeRayTube()
            file.forEachLine {
                when {
                    it == "noop" -> cathodeRayTube.noop()
                    it.startsWith("addx") -> {
                        val value = it.takeLastWhile { it != ' ' }
                        cathodeRayTube.addX(value.toInt())
                    }
                }
            }
            cathodeRayTube.printCrt()
        }
    }
}