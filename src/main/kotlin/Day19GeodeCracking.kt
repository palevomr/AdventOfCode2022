import java.io.File
import java.util.*

class Day19GeodeCracking {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("./src/main/resources/input_day_19")
            val regexNumber = Regex("\\d+")
            val blueprints = file.readLines().map {
                val iterator = regexNumber.findAll(it).map { it.value.toInt() }.iterator()
                Blueprint(
                    id = iterator.next(),
                    oreRobotCost = iterator.next(),
                    clayRobotCost = iterator.next(),
                    obsidianRobotOreCost = iterator.next(),
                    obsidianRobotClayCost = iterator.next(),
                    geodeRobotOreCost = iterator.next(),
                    geodeRobotObsidianCost = iterator.next()
                )
            }.toList()
            println(blueprints.take(3).map {
                val rank = it.rank(32)
                rank
            })
        }
    }
}

data class State(
    var oreRobotCount: Int,
    var oreCount: Int = 0,
    var clayRobotCount: Int = 0,
    var clayCount: Int = 0,
    var obsidianRobotCount: Int = 0,
    var obsidianCount: Int = 0,
    var geodeRobotCount: Int = 0,
    var geodeCount: Int = 0,
    val timeLeft: Int,
) {
    fun canBuyGeodeRobot(orePrice: Int, obsidianPrice: Int): Boolean {
        return obsidianCount >= obsidianPrice && oreCount >= orePrice
    }

    fun buyGeodeRobot(orePrice: Int, obsidianPrice: Int): State {
        return copy(
            oreCount = oreCount + oreRobotCount - orePrice,
            clayCount = clayCount + clayRobotCount,
            obsidianCount = obsidianCount - obsidianPrice + obsidianRobotCount,
            geodeCount = geodeCount + geodeRobotCount,
            geodeRobotCount = geodeRobotCount + 1,
            timeLeft = timeLeft - 1
        )
    }

    fun canBuyObsidianRobot(orePrice: Int, clayPrice: Int): Boolean {
        return clayCount >= clayPrice && oreCount >= orePrice
    }

    fun buyObsidianRobot(orePrice: Int, clayPrice: Int): State {
        return copy(
            oreCount = oreCount + oreRobotCount - orePrice,
            clayCount = clayCount + clayRobotCount - clayPrice,
            obsidianCount = obsidianCount + obsidianRobotCount,
            geodeCount = geodeCount + geodeRobotCount,
            obsidianRobotCount = obsidianRobotCount + 1,
            timeLeft = timeLeft - 1
        )
    }

    fun canBuyClayRobot(orePrice: Int): Boolean {
        return oreCount >= orePrice
    }

    fun buyClayRobot(orePrice: Int): State {
        return copy(
            oreCount = oreCount + oreRobotCount - orePrice,
            clayCount = clayCount + clayRobotCount,
            obsidianCount = obsidianCount + obsidianRobotCount,
            geodeCount = geodeCount + geodeRobotCount,
            clayRobotCount = clayRobotCount + 1,
            timeLeft = timeLeft - 1
        )
    }

    fun canBuyOreRobot(orePrice: Int): Boolean {
        return oreCount >= orePrice
    }

    fun buyOreRobot(orePrice: Int): State {
        return copy(
            oreCount = oreCount + oreRobotCount - orePrice,
            clayCount = clayCount + clayRobotCount,
            obsidianCount = obsidianCount + obsidianRobotCount,
            geodeCount = geodeCount + geodeRobotCount,
            oreRobotCount = oreRobotCount + 1,
            timeLeft = timeLeft - 1
        )
    }

    fun skip(): State {
        return copy(
            oreCount = oreCount + oreRobotCount,
            clayCount = clayCount + clayRobotCount,
            obsidianCount = obsidianCount + obsidianRobotCount,
            geodeCount = geodeCount + geodeRobotCount,
            timeLeft = timeLeft - 1
        )

    }
}

data class Blueprint(
    val id: Int,
    val oreRobotCost: Int,
    val clayRobotCost: Int,
    val obsidianRobotOreCost: Int,
    val obsidianRobotClayCost: Int,
    val geodeRobotOreCost: Int,
    val geodeRobotObsidianCost: Int,
) {

    val oreMaxCost = maxOf(oreRobotCost, clayRobotCost, obsidianRobotOreCost, geodeRobotOreCost)

    fun rank(maxTimeLeft: Int = 24): Int {
        val stack = Stack<State>()
        stack.push(State(oreRobotCount = 1, timeLeft = maxTimeLeft))
        val seen = HashSet<State>()
        var best = 0

        while (stack.isNotEmpty()) {
            val current = stack.pop()
            if (seen.contains(current)) continue
            val timeLeft = current.timeLeft
            if (timeLeft == 0) {
                best = maxOf(best, current.geodeCount)
                continue
            }
            if ((timeLeft * timeLeft + timeLeft) / 2 + current.geodeRobotCount * timeLeft <= best - current.geodeCount) continue

            if (current.canBuyGeodeRobot(geodeRobotOreCost, geodeRobotObsidianCost)) {
                stack.push(current.buyGeodeRobot(geodeRobotOreCost, geodeRobotObsidianCost))
                continue
            }
            if (current.oreRobotCount <= oreMaxCost && current.canBuyOreRobot(oreRobotCost)) {
                stack.push(current.buyOreRobot(oreRobotCost))
            }
            if (current.clayRobotCount <= obsidianRobotClayCost && current.canBuyClayRobot(clayRobotCost)) {
                stack.push(current.buyClayRobot(clayRobotCost))
            }
            if (current.obsidianCount <= geodeRobotObsidianCost
                && current.canBuyObsidianRobot(obsidianRobotOreCost, obsidianRobotClayCost)
            ) {
                stack.push(current.buyObsidianRobot(obsidianRobotOreCost, obsidianRobotClayCost))
            }
            stack.push(current.skip())
            seen.add(current)
        }
        return best
    }
}