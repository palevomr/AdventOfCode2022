import java.io.File
import java.util.*

class Day13DistressSignal {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            part1()
            part2()
        }

        private fun part2() {
            val distressSignal = Day13DistressSignal()
            val marker2 = distressSignal.processInput("[[2]]")
            val marker6 = distressSignal.processInput("[[6]]")
            distressSignal.addItem(marker2)
            distressSignal.addItem(marker6)
            File("./src/main/resources/input_day_13").forEachLine {
                if (it.isNotEmpty()) {
                    distressSignal.addItem(it)
                }
            }
            distressSignal.sortItems()
            println((distressSignal.items.indexOf(marker2) + 1) * (distressSignal.items.indexOf(marker6) + 1))
        }

        private fun part1() {
            val distressSignal = Day13DistressSignal()
            val file = File("./src/main/resources/input_day_13")
            file.forEachLine {
                if (it.isNotEmpty()) {
                    distressSignal.addItem(it)
                }
            }
            distressSignal.validatePairs()
        }
    }

    private val items = mutableListOf<Packet>()

    fun addItem(input: String) {
        items.add(processInput(input))
    }

    fun addItem(packet: Packet) {
        items.add(packet)
    }

    fun sortItems() {
        items.sortWith { a, b ->
            validatePair(a, b)
        }
    }

    fun validatePairs() {
        val validIndices = mutableSetOf<Int>()
        items.chunked(2).forEachIndexed { index, pair ->
            if (validatePair(pair[0], pair[1]) == -1) {
                validIndices.add(index + 1)
            }
        }
        println(validIndices.sum())
    }

    private fun validatePair(first: Packet, second: Packet): Int {
        return when {
            first is Packet.IntValue && second is Packet.IntValue -> first.value.compareTo(second.value)
            else -> {
                val leftPacketList = if (first is Packet.ListValue) first else Packet.ListValue(listOf(first))
                val rightPacketList =  if (second is Packet.ListValue) second else Packet.ListValue(listOf(second))

                if (leftPacketList.items.isEmpty() && rightPacketList.items.isEmpty()) {
                    0
                } else {
                    var index = 0
                    var order = 0
                    while (order == 0) {
                        val leftChild = leftPacketList.items.getOrNull(index)
                        val rightChild = rightPacketList.items.getOrNull(index)

                        order = if (leftChild != null && rightChild != null) {
                            validatePair(leftChild, rightChild)
                        } else if (leftChild == null && rightChild == null) {
                            break
                        } else if (leftChild == null) -1 else 1

                        index++
                    }
                    order
                }

            }
        }
    }

    fun processInput(input: String): Packet {
        val parent = mutableListOf<Packet>()
        val parentStack = Stack<MutableList<Packet>>()
        var currentList = parent
        var i = 1
        while (i < input.length - 1) {
            if (input[i].isDigit()) {
                var j = i
                while (input[j].isDigit()) {
                    j++
                }
                currentList.add(Packet.IntValue(input.slice(i until j).toInt()))
                i += (j - i)
            } else if (input[i] == '[') {
                parentStack.push(currentList)
                val temp = mutableListOf<Packet>()
                currentList.add(Packet.ListValue(temp))
                currentList = temp
                i++
            } else if (input[i] == ']') {
                currentList = parentStack.pop()
                i++
            } else {
                i++
            }
        }
        return Packet.ListValue(parent)
    }

    fun printList() {
        items.forEach {
            println("$it")
        }
    }
}

interface Packet {
    data class IntValue(val value: Int): Packet
    data class ListValue(val items: List<Packet>): Packet
}