package day20

import getResourceAsText

fun main() {
    val descriptions = getResourceAsText("/day20/input.txt").lines()
        .map { parseModuleDescription(it) }

    val result = solve(descriptions)
    println(result)
}

private fun solve(descriptions: List<ModuleDescription>): Int {
    val modules = buildModules(descriptions).associateBy { it.name }

    var lowPulses = 0
    var highPulses = 0

    repeat(1000) {
        val queue = ArrayDeque<Pulse>()
        queue.addLast(Pulse("button", "broadcaster", false)) // button press

        while (queue.isNotEmpty()) {
            val (from, to, high) = queue.removeFirst()

            if (high) {
                highPulses++
            } else {
                lowPulses++
            }

            val toModule = modules[to] ?: continue

            toModule.trigger(from, high) { newTo, newHigh -> queue.addLast(Pulse(to, newTo, newHigh)) }
        }
    }

    val result = lowPulses * highPulses
    return result
}