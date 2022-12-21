import java.io.File
import kotlin.math.abs

// inspired by https://github.com/tbpaolini/Advent-of-Code/blob/master/2022/Day%2020/day_20.c
class Day20GrovePositioningSystem {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_20")
            val input = file.readLines().map { DoubleLinkedListNode(it.toInt() * 811589153L, null, null) }
            val n = input.size
            var startPosition: DoubleLinkedListNode? = null
            for (i in input.indices) {
                input[i].previous = if (i == 0) input.last() else input[i - 1]
                input[i].next = if (i == input.size - 1) input[0] else input[i + 1]
                if (input[i].value == 0L) startPosition = input[i]
            }

            repeat(10) {
                for (i in 0 until n) {
                    val current = input[i]
                    var head = current

                    val steps = (current.value % (n - 1)).let {
                        val backwardSteps = (n - 1) + it
                        if (abs(backwardSteps) < abs(it)) backwardSteps else it
                    }.toInt()

                    if (steps == 0) continue

                    head.previous?.next = head.next
                    head.next?.previous = head.previous
                    head = head.previous!!

                    repeat(abs(steps)) {
                        head = if (steps > 0) head.next!! else head.previous!!
                    }

                    current.previous = head
                    current.next = head.next
                    head.next?.previous = current
                    head.next = current
                }
            }

            var ans = 0L
            repeat(3) {
                var current = startPosition
                repeat(((it + 1) * 1000) % n) {
                    current = current?.next
                }
                ans += current?.value ?: 0
            }
            println("Answer - $ans")
        }

    }

    data class DoubleLinkedListNode(
        val value: Long,
        var next: DoubleLinkedListNode?,
        var previous: DoubleLinkedListNode?
    ) {
        override fun toString(): String {
            return "$value"
        }
    }
}