import java.io.File
import java.util.*

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
            pipeSystem.valves.values.forEach {
                it.initPaths(pipeSystem.valves)
            }
            pipeSystem.valves.values.forEach {
                it.initWithUseful(pipeSystem.useful)
                println(it.ways)
            }
            val time = System.currentTimeMillis()
            println(pipeSystem.findOptimalWayWithElephant())
            val elapsed = System.currentTimeMillis() - time
            println("Elapsed time - $elapsed ms")
        }
    }

    private val valves = mutableMapOf<String, Valve>()
    private val useful = mutableSetOf<Valve>()

    fun addPipe(pipeName: String, pipeValue: Int, tunnelsTo: List<String>) {
        valves[pipeName] = Valve(pipeName, pipeValue, tunnelsTo).also {
            if (pipeValue > 0) useful.add(it)
        }
    }

    fun findOptimalWayWithElephant(): Int {
        val memo = mutableMapOf<String, Int>()
        val shouldOpen = mutableSetOf<String>()
        useful.forEach { shouldOpen.add(it.id) }
        println(shouldOpen)
        fun traverse(human: Actor, elephant: Actor, totalRelease: Int): Int {
            val key = "$human$elephant+$totalRelease"
            if (memo[key] != null) return memo[key]!!
            if (human.leftMinutes <= 0 && elephant.leftMinutes <= 0) return totalRelease
            if (shouldOpen.isEmpty()) return totalRelease
            var max = totalRelease
            if (elephant.leftMinutes > human.leftMinutes) {
                val currentValve = elephant.currentValve
                currentValve.ways.forEach { (valve, value) ->
                    if (elephant.leftMinutes - value > 0 && shouldOpen.contains(valve.id)) {
                        shouldOpen.remove(valve.id)
                        val newlyReleased = (elephant.leftMinutes - value) * valve.rate
                        val result = traverse(
                            human,
                            elephant.copy(currentValve = valve, leftMinutes = elephant.leftMinutes - value),
                            totalRelease + newlyReleased
                        )
                        shouldOpen.add(valve.id)
                        max = Math.max(result, max)
                    }
                }
            } else {
                human.currentValve.ways.forEach { (valve, value) ->
                    if (human.leftMinutes - value > 0 && shouldOpen.contains(valve.id)) {
                        shouldOpen.remove(valve.id)
                        val newlyReleased = (human.leftMinutes - value) * valve.rate
                        val result = traverse(
                            human.copy(currentValve = valve, leftMinutes = human.leftMinutes - value),
                            elephant,
                            totalRelease + newlyReleased
                        )
                        shouldOpen.add(valve.id)
                        max = Math.max(result, max)
                    }
                }
            }
            memo[key] = max
            return max
        }
        return traverse(Actor(valves["AA"]!!, 26), Actor(valves["AA"]!!, 26), 0)
    }
}

data class Actor(
    var currentValve: Valve,
    var leftMinutes: Int,
) {
    override fun toString(): String {
        return "${currentValve.id}+$leftMinutes"
    }
}

data class Valve(
    val id: String,
    val rate: Int,
    val directionsRaw: List<String>
) {
    val directions = mutableListOf<Valve>()
    val ways = mutableMapOf<Valve, Int>()

    override fun toString(): String {
        return "$id"
    }
    fun initPaths(map: Map<String, Valve>) {
        directionsRaw.forEach {
            directions.add(map[it]!!)
        }
    }

    fun initWithUseful(useful: Set<Valve>) {
        val stack = LinkedList<Valve>()
        val distances = HashMap<Valve, Int>()
        val visited = HashSet<Valve>()
        stack.add(this)
        distances[this] = 0
        while (!stack.isEmpty() && ways.size < useful.size) {
            val cur = stack.removeAt(0)
            visited.add(cur)
            val curDist = distances[cur]
            if (cur !== this && useful.contains(cur)) {
                ways[cur] = curDist!! + 1 // the way takes curDist minutes; +1 minute for opening the valve
            }
            for (valve in cur.directions) {
                if (!visited.contains(valve)) {
                    stack.add(valve)
                    distances[valve] = curDist!! + 1
                }
            }
        }
    }
}