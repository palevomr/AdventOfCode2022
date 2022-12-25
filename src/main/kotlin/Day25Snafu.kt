import java.io.File
import kotlin.math.pow

object SnafuConverter {

    private fun snafuToDecimal(snafu: String): Long {
        var result = 0L
        snafu.reversed().forEachIndexed { index, c ->
            val current = 5.0.pow(index)
            val factor = when (c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException()
            }
            result += current.toLong() * factor
        }
        return result
    }

    private fun decimalToSnafu(num: Long): String {
        val builder = StringBuilder()
        var num = num
        while (num != 0L) {
            when (num % 5) {
                0L -> {
                    builder.append("0")
                    num /= 5L
                }

                1L -> {
                    builder.append("1")
                    num /= 5L
                }

                2L -> {
                    builder.append("2")
                    num /= 5L
                }

                3L -> {
                    builder.append("=")
                    num /= 5L
                    num++
                }

                4L -> {
                    builder.append("-")
                    num /= 5L
                    num++
                }
            }
        }
        return builder.reversed().toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val file = File("./src/main/resources/input_day_25")
        val input = file.readLines()
        println("Result is ${decimalToSnafu(input.sumOf { snafuToDecimal(it) })}")
    }
}