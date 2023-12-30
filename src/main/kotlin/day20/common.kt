package day20

fun buildModules(descriptions: List<ModuleDescription>): List<Module> {
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

fun parseModuleDescription(s: String): ModuleDescription {
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

data class Pulse(val from: String, val to: String, val high: Boolean)

data class ModuleDescription(val name: String, val type: ModuleType, val destinations: List<String>)

enum class ModuleType {
    FLIP_FLOP, CONJUNCTION, BROADCAST
}

sealed interface Module {
    val name: String
    val destinations: List<String>

    fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit)
    fun dumpState(): String
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

    override fun dumpState(): String {
        return state.toString()
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

    override fun dumpState(): String {
        return states.toString()
    }

}

private class BroadcasterModule(
    override val name: String,
    override val destinations: List<String>
) : Module {
    override fun trigger(from: String, high: Boolean, send: (to: String, high: Boolean) -> Unit) {
        destinations.forEach { send(it, high) }
    }

    override fun dumpState(): String {
        return "-"
    }

}