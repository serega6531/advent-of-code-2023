package day17

import getResourceAsText

fun main() {
    val field: List<List<Int>> = getResourceAsText("/day17/input.txt")
        .lines()
        .map { line -> line.map { it.digitToInt() } }

    val result = solve(field, ::nextDirections) { true }
    println(result)
}

private fun nextDirections(path: PossiblePath): List<Direction> {
    return if (path.consecutiveSteps == 3) {
        path.direction.turns
    } else {
        path.direction.turns + path.direction
    }
}