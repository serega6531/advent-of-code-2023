package day21

import YX
import getResourceAsText

fun main() {
    val field = getResourceAsText("/day21/input.txt").lines()
    val steps = 26501365

    val start = findStart(field)

    val seq = generateSequence(setOf(start)) { previous -> findPossibleSteps(field, previous) }
    val positions = seq.withIndex().first { (index, _) -> index == steps }.value
    val result = positions.size
    println(result)
}

private fun findPossibleSteps(field: List<String>, previous: Set<YX>): Set<YX> {
    return buildSet {
        previous.forEach { pos ->
            Direction.entries.forEach { direction ->
                val potentialY = pos.first + direction.dy
                val potentialX = pos.second + direction.dx

                val tile = field.getRepeated(potentialY).getRepeated(potentialX)
                if (tile == 'S' || tile == '.') {
                    add(potentialY to potentialX)
                }
            }
        }
    }
}

private fun <T> List<T>.getRepeated(index: Int): T {
    val actual = index.mod(size)
    return this[actual]
}

private fun String.getRepeated(index: Int): Char {
    val actual = index.mod(length)
    return this[actual]
}

