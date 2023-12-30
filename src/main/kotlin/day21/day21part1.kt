package day21

import YX
import getResourceAsText

fun main() {
    val field = getResourceAsText("/day21/input.txt").lines()
    val steps = 64

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

                val tile = field.getOrNull(potentialY)?.getOrNull(potentialX)
                if (tile == 'S' || tile == '.') {
                    add(potentialY to potentialX)
                }
            }
        }
    }
}
