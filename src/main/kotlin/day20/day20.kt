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

    val queue = ArrayDeque<Pulse>()
    var lowPulses = 0
    var highPulses = 0

    repeat(1000) {
        queue.addLast(Pulse("button", "broadcaster", false)) // button press

        while (queue.isNotEmpty()) {
            val (from, to, high) = queue.removeFirst()

            if (high) {
                highPulses++
            } else {
                lowPulses++
            }

            val toModule = modules[to]

            if (toModule == null) {
                println("Unknown target module $to")
                continue
            }

            toModule.trigger(from, high) { newTo, newHigh -> queue.addLast(Pulse(to, newTo, newHigh)) }
        }
    }

    val result = lowPulses * highPulses
    return result
}

private fun buildModules(descriptions: List<ModuleDescription>): List<Module> {
    val sources = getSources(descriptions)

    return descriptions.map { buildModule(it, sources.getValue(it.name)) }
}

private fun buildModule(description: ModuleDescription, sources: List<String>): Module {
    return when (description.type) {
        ModuleType.FLIP_FLOP -> FlipFlopModule(description.name, description.destinations)
        ModuleType.CONJUNCTION -> ConjunctionModule(description.name, description.destinations, sources)
        ModuleType.BROADCAST -> BroadcasterModule(description.name, description.destinations)
    }
}

private fun parseModuleDescription(s: String): ModuleDescription {
    val (name, destinationsString) = s.split(" -> ")
    val destinations = destinationsString.split(", ")

    val (type, clearedName) = when {
        name.startsWith("%") -> ModuleType.FLIP_FLOP to name.drop(1)
        name.startsWith("&") -> ModuleType.CONJUNCTION to name.drop(1)
        name == "broadcaster" -> ModuleType.BROADCAST to name
        else -> throw IllegalArgumentException()
    }

    return ModuleDescription(clearedName, type, destinations)
}

private fun getSources(descriptions: List<ModuleDescription>): Map<String, List<String>> {
    return descriptions.map { it.name }
        .associateWith { target ->
            descriptions.filter { other -> other.destinations.contains(target) }
                .map { it.name }
        }
}

private data class Pulse(val from: String, val to: String, val high: Boolean)

private data class ModuleDescription(val name: String, val type: ModuleType, val destinations: List<String>)

private enum class ModuleType {
    FLIP_FLOP, CONJUNCTION, BROADCAST
}

private sealed interface Module {
    val name: String
    val destinations: List<String>

    fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit)
}

private class FlipFlopModule(
    override val name: String,
    override val destinations: List<String>
) : Module {
    private var state: Boolean = false

    override fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit) {
        if (!high) {
            state = !state
            destinations.forEach { send(it, state) }
        }
    }
}

private class ConjunctionModule(
    override val name: String,
    override val destinations: List<String>,
    inputs: List<String>
) : Module {

    private val states = inputs.associateWith { false }.toMutableMap()

    override fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit) {
        states[from] = high

        if (states.all { (_, high) -> high }) {
            destinations.forEach { send(it, false) }
        } else {
            destinations.forEach { send(it, true) }
        }
    }

}

private class BroadcasterModule(
    override val name: String,
    override val destinations: List<String>
) : Module {
    override fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit) {
        destinations.forEach { send(it, high) }
    }

}