package day17

import YX
import getResourceAsText

fun main() {
    val field: List<List<Int>> = getResourceAsText("/day17/input.txt")
        .lines()
        .map { line -> line.map { it.digitToInt() } }

    val result = solve(field, ::nextDirections) { path -> validatePath(path, field) }
    println(result)
}

private fun nextDirections(path: PossiblePath): List<Direction> {
    return when (path.consecutiveSteps) {
        in 1..3 -> listOf(path.direction)
        in 4..9 -> path.direction.turns + path.direction
        10 -> path.direction.turns
        else -> throw IllegalStateException()
    }
}

private fun validatePath(path: PossiblePath, field: List<List<Int>>): Boolean {
    val target = YX(field.lastIndex, field[0].lastIndex)

    return path.position != target || path.consecutiveSteps in 4..10
}