package day22

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day22/input.txt").lines()
        .map { it.split('~') }
        .map { (start, end) -> start.toXYZ() to end.toXYZ() }

    val sorted = input
        .map { it.sortedByZ() }
        .sortedBy { (first, _) -> first.z }

    val blocksUsed = placeBlocks(sorted)

    val (indexToSupports, indexToSupportedBy) = calculateSupports(blocksUsed)

    val result = sorted.indices.sumOf { index ->
        tryDisintegrate(listOf(index), indexToSupports, indexToSupportedBy)
    }

    println(result)
}

private tailrec fun tryDisintegrate(
    indicies: List<Int>,
    indexToSupports: Map<Int, Set<Int>>,
    indexToSupportedBy: Map<Int, Set<Int>>,
    disintegrated: MutableSet<Int> = mutableSetOf()
): Int {
    disintegrated.addAll(indicies)

    val supports = indicies.flatMapTo(HashSet()) { indexToSupports[it] ?: emptySet() }
    val toDisintegrate = supports.filter { supportedBrick ->
        (indexToSupportedBy.getValue(supportedBrick) - disintegrated).isEmpty()
    }

    if (toDisintegrate.isEmpty()) {
        return disintegrated.size - 1
    }

    return tryDisintegrate(toDisintegrate, indexToSupports, indexToSupportedBy, disintegrated)
}

