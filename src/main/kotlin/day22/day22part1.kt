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

    val result = sorted.indices.count { index ->
        val supports = indexToSupports[index] ?: emptySet()
        supports.all { supportedBrick ->
            indexToSupportedBy.getValue(supportedBrick).size > 1
        }
    }

    println(result)
}

