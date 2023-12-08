package day8

import getResourceAsText

fun main() {
    val (instructions, nodesString) = getResourceAsText("/day8/input.txt").split("\n\n")

    val nodeRegex = Regex("""([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)""")

    val nodes = nodesString.lines()
        .map { nodeRegex.matchEntire(it)!! }
        .map { it.destructured }
        .associate { (source, left, right) -> source to (left to right) }

    val starts = nodes.keys.filter { it.endsWith('A') }

    val sequences = starts
        .map { getNodeSequence(it, instructions, nodes) }
        .map { it.iterator() }

    val result = generateSequence({ starts.map { it to 0L } }, { sequences.advanceAll() })
        .first { it.all { node -> node.first.endsWith('Z') } }
        .first()
        .second

    println(result)
}

private fun <T> List<Iterator<T>>.advanceAll(): List<T> {
    return this.map { it.next() }
}
