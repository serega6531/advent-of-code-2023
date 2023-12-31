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

    val indexToSupports = mutableMapOf<Int, MutableSet<Int>>()
    val indexToSupportedBy = mutableMapOf<Int, MutableSet<Int>>()

    blocksUsed.forEach { (block, brickIndex) ->
        val brickAbove = blocksUsed[block.copy(z = block.z + 1)]
        if (brickAbove != null && brickAbove != brickIndex) {
            indexToSupports.computeIfAbsent(brickIndex, ::HashSet).add(brickAbove)
        }

        val brickBellow = blocksUsed[block.copy(z = block.z - 1)]
        if (brickBellow != null && brickBellow != brickIndex) {
            indexToSupportedBy.computeIfAbsent(brickIndex, ::HashSet).add(brickBellow)
        }
    }

    val result = sorted.indices.count { index ->
        val supports = indexToSupports[index] ?: emptySet()
        supports.all { supportedBrick ->
            indexToSupportedBy.getValue(supportedBrick).size > 1
        }
    }

    println(result)
}

private fun placeBlocks(sorted: List<Pair<XYZ, XYZ>>): Map<XYZ, Int> {
    val blocksUsed = mutableMapOf<XYZ, Int>()

    sorted.forEachIndexed { brickIndex, (first, second) ->
        val movedBlocks = (1..Int.MAX_VALUE).asSequence()
            .map { potentialZ ->
                val diffZ = first.z - potentialZ
                val newFirst = first.copy(z = potentialZ)
                val newSecond = second.copy(z = second.z - diffZ)

                Pair(newFirst, newSecond) to getAllBlocks(newFirst, newSecond)
            }
            .first { (newBrick, blocks) ->
                getProgression(second.z, newBrick.first.z).all { level ->
                    val blocksAtLevel = blocks.mapTo(HashSet()) { it.copy(z = level) }
                    blocksUsed.keys.intersect(blocksAtLevel).isEmpty()
                }
            }
            .second

        blocksUsed.putAll(movedBlocks.associateWith { brickIndex })
    }

    return blocksUsed
}

private fun String.toXYZ(): XYZ {
    val (x, y, z) = split(',').map { it.toInt() }
    return XYZ(x, y, z)
}

private fun Pair<XYZ, XYZ>.sortedByZ(): Pair<XYZ, XYZ> {
    return if (this.first.z < this.second.z) {
        this
    } else {
        Pair(this.second, this.first)
    }
}

private fun getAllBlocks(first: XYZ, second: XYZ): Set<XYZ> {
    return getProgression(first.x, second.x).flatMapTo(HashSet()) { x ->
        getProgression(first.y, second.y).flatMap { y ->
            getProgression(first.z, second.z).map { z ->
                XYZ(x, y, z)
            }
        }
    }
}

private fun getProgression(from: Int, to: Int): IntProgression {
    val step = if (to - from > 0) 1 else -1
    return IntProgression.fromClosedRange(from, to, step)
}

private data class XYZ(
    val x: Int,
    val y: Int,
    val z: Int
)