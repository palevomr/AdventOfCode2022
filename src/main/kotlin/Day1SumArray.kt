import java.util.PriorityQueue

object Day1SumArray {
    fun findMax(input: List<List<Int>>): Int {
        var sumMax = 0
        input.forEach {
            sumMax = maxOf(sumMax, it.sum())
        }
        return sumMax
    }

    fun finMaxTopK(input: List<List<Int>>, k: Int): List<Int> {
        val priorityQueue = PriorityQueue<Int> { a, b ->
            a - b
        }
        input.forEach {
            priorityQueue.add(it.sum())
            if (priorityQueue.size > k) {
                priorityQueue.poll()
            }
        }
        return priorityQueue.toList()
    }
}
