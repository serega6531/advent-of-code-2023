package day20

import getResourceAsText

fun main() {
    val descriptions = getResourceAsText("/day20/input.txt").lines()
        .map { parseModuleDescription(it) }

    val result = solve(descriptions)
    println(result)
}

private fun solve(descriptions: List<ModuleDescription>): Long {
    val modules = buildModules(descriptions).associateBy { it.name }

    val result = (Int.MAX_VALUE..Long.MAX_VALUE).first { press ->
        val queue = ArrayDeque<Pulse>()
        queue.addLast(Pulse("button", "broadcaster", false)) // button press

        while (queue.isNotEmpty()) {
            val (from, to, high) = queue.removeFirst()

            if (to == "rx" && !high) {
                return@first true
            }

            val toModule = modules[to] ?: continue

            toModule.trigger(from, high) { newTo, newHigh -> queue.addLast(Pulse(to, newTo, newHigh)) }
        }

//        val dump = modules.values.joinToString(separator = "\n") { module -> "${module.name}: ${module.dumpState()}" }
//        println("$press:\n$dump\n\n")

        return@first false
    }

    return result
}