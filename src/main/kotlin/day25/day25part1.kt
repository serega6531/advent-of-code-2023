package day25

import getResourceAsText

fun main() {
    val uniqueConnections = getResourceAsText("/day25/input.txt").lines()
        .map { it.split(": ") }
        .map { (source, targets) -> source to targets.split(" ") }
        .flatMap { (source, targets) -> targets.map { source to it } }

    val connections = buildConnectionsMap(uniqueConnections)

    while (true) {
        val (group1, group2, cutConnections) = trySolve(uniqueConnections, connections)

        if (cutConnections.size == 3) {
            println(group1.size * group2.size)
            return
        }
    }

}

private fun trySolve(uniqueConnections: List<Pair<String, String>>, connections: Map<String, Set<String>>): Solution {
    val nodesConnectionsLeft: MutableMap<CombinedNode, MutableSet<CombinedNode>> = connections.map { (k, v) -> setOf(k) to v.map { setOf(it) }.toMutableSet() }.toMap(LinkedHashMap())

    while (nodesConnectionsLeft.size > 2) {
        val randomNode1 = nodesConnectionsLeft.keys.random()
        val connected1 = nodesConnectionsLeft.getValue(randomNode1)
        val randomNode2 = connected1.random()
        val connected2 = nodesConnectionsLeft.getValue(randomNode2)

        val newNode = randomNode1 + randomNode2
        nodesConnectionsLeft.remove(randomNode1)
        nodesConnectionsLeft.remove(randomNode2)

        val newConnections = mutableSetOf<CombinedNode>().apply {
            addAll(connected1)
            addAll(connected2)
            remove(randomNode1)
            remove(randomNode2)
        }

        nodesConnectionsLeft[newNode] = newConnections

        replaceReverseConnection(connected1 - randomNode2, nodesConnectionsLeft, randomNode1, newNode)
        replaceReverseConnection(connected2 - randomNode1, nodesConnectionsLeft, randomNode2, newNode)
    }

    val (group1, group2) = nodesConnectionsLeft.keys.toList()

    val cutConnections = uniqueConnections.filter { (first, second) ->
        val firstInFirstGroup = group1.contains(first)
        val secondInFirstGroup = group1.contains(second)

        firstInFirstGroup != secondInFirstGroup // sides of connection are in different groups

    }

    return Solution(
        group1 = group1,
        group2 = group2,
        cutConnections = cutConnections
    )
}

private operator fun Set<CombinedNode>.minus(toRemove: CombinedNode): Set<CombinedNode> {
    // had to write this because of typesystem issue
    return this.filterNotTo(HashSet()) { it == toRemove }
}

private fun replaceReverseConnection(
    oldConnections: Set<CombinedNode>,
    connections: MutableMap<CombinedNode, MutableSet<CombinedNode>>,
    oldNode: CombinedNode,
    newNode: CombinedNode
) {
    oldConnections.forEach {
        val reverseConnections = connections.getValue(it)
        reverseConnections.remove(oldNode)
        reverseConnections.add(newNode)
    }
}

private fun buildConnectionsMap(input: List<Pair<String, String>>): Map<String, Set<String>> {
    return buildMap<String, MutableSet<String>> {
        input.forEach { (first, second) ->
            val firstList = getOrPut(first) { mutableSetOf() }
            firstList.add(second)

            val secondList = getOrPut(second) { mutableSetOf() }
            secondList.add(first)
        }
    }
}

private data class Solution(
    val group1: CombinedNode,
    val group2: CombinedNode,
    val cutConnections: List<Pair<String, String>>,
)

private typealias CombinedNode = Set<String>
