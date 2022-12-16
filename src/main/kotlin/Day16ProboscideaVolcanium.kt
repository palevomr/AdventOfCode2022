import java.io.File

class PipeSystem() {
    companion object {

        private val regex = Regex("[A-Z]{2}")
        private val regexNumber = Regex("\\d+")

        @JvmStatic
        fun main(args: Array<String>) {
            val pipeSystem = PipeSystem()
            val file = File("./src/main/resources/input_day_16")
            file.forEachLine {
                val pipes = regex.findAll(it).map { it.value }.toList()
                pipeSystem.addPipe(pipes[0], regexNumber.find(it)?.value?.toInt() ?: 0, pipes.takeLast(pipes.size - 1))
            }
            val time = System.currentTimeMillis()
            println(pipeSystem.findOptimalWay())
            val elapsed = System.currentTimeMillis() - time
            println("Elapsed time - $elapsed ms")
        }
    }

    private val pipesTunnelsMap = mutableMapOf<String, List<String>>()
    private val pipeValueMap = mutableMapOf<String, Int>()

    fun addPipe(pipeName: String, pipeValue: Int, tunnelsTo: List<String>) {
        pipesTunnelsMap[pipeName] = tunnelsTo
        pipeValueMap[pipeName] = pipeValue
        println("Add $pipeName with $pipeValue, which leads to $tunnelsTo")
    }

    fun findOptimalWay(): Int {
        val opened = mutableMapOf<String, Boolean>()
        val memo = mutableMapOf<String, Int>()
        fun traverse(
            currentPipe: String,
            leftMinutes: Int,
            totalValue: Int,
            currentOpenedValue: Int
        ): Int {
            if (memo["$currentPipe+$leftMinutes+$totalValue"] != null) {
                return memo["$currentPipe+$leftMinutes+$totalValue"]!!
            }
            if (leftMinutes <= 0) {
                return totalValue
            }
            var max = 0
            val directions = pipesTunnelsMap[currentPipe].orEmpty()
            val currentValue = pipeValueMap[currentPipe] ?: 0
            directions.forEach {
                val openCurrentPipe = if (currentValue != 0
                    && !opened.getOrDefault(currentPipe, false)
                    && leftMinutes != 1
                ) {
                    opened[currentPipe] = true
                    val result = traverse(
                        currentPipe = it,
                        leftMinutes = leftMinutes - 2,
                        totalValue = totalValue + 2 * currentOpenedValue + currentValue,
                        currentOpenedValue = currentOpenedValue + currentValue
                    )
                    opened[currentPipe] = false
                    result
                } else 0
                val skipCurrentPipe = traverse(
                    currentPipe = it,
                    leftMinutes = leftMinutes - 1,
                    totalValue = totalValue + currentOpenedValue,
                    currentOpenedValue = currentOpenedValue
                )
                max = Math.max(max, Math.max(openCurrentPipe, skipCurrentPipe))
            }
            memo["$currentPipe+$leftMinutes+$totalValue"] = max
            return max
        }
        return traverse("AA", 30, 0, 0)
    }
}

data class Pipe(
    val rate: Int,
    var opened: Boolean,
    val leadsTo: Set<Pipe>
)