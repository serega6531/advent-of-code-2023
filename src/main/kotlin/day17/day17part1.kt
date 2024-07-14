package day17

import YX
import getResourceAsText
import java.util.Comparator.comparingInt
import java.util.PriorityQueue

fun main() {
    val field: List<List<Int>> = getResourceAsText("/day17/input.txt")
        .lines()
        .map { line -> line.map { it.digitToInt() } }

    val result = solve(field)
    println(result)
}

private fun solve(field: List<List<Int>>): Int {
    val queue = PriorityQueue<PossiblePath>()
    val minCosts = mutableMapOf<PossiblePathKey, Int>()

    val target = YX(field.lastIndex, field[0].lastIndex)

    listOf(Direction.RIGHT, Direction.DOWN).forEach { direction ->
        queue += PossiblePath(
            direction = direction,
            consecutiveSteps = 1,
            cost = field[direction.dy][direction.dx],
            position = YX(direction.dy, direction.dx)
        )
    }

    while (queue.isNotEmpty()) {
        val path = queue.remove()

        if (target == path.position) {
            return path.cost
        }

        val nextPaths = path.nextPaths(field)
            .filter { nextPath -> nextPath.cost.lessThanIgnoringNull(minCosts[nextPath.toKey()]) }

        minCosts.putAll(nextPaths.associate { it.toKey() to it.cost })
        queue.addAll(nextPaths)
    }

    throw IllegalStateException()
}

private fun Int.lessThanIgnoringNull(other: Int?) = other == null || this < other

private data class PossiblePath(
    val direction: Direction,
    val consecutiveSteps: Int,
    val cost: Int,
    val position: YX
) : Comparable<PossiblePath> {

    fun nextPaths(field: List<List<Int>>): List<PossiblePath> {
        val nextDirections = if (consecutiveSteps == 3) {
            direction.turns
        } else {
            direction.turns + direction
        }

        return nextDirections
            .filter { newDirection -> (position + newDirection) in field }
            .map { newDirection ->
                PossiblePath(
                    direction = newDirection,
                    consecutiveSteps = if (newDirection == direction) consecutiveSteps + 1 else 1,
                    cost = cost + field[position + newDirection],
                    position = position + newDirection
                )
            }
    }

    fun toKey() = PossiblePathKey(
        direction, consecutiveSteps, position
    )

    override fun compareTo(other: PossiblePath): Int {
        return comparingInt<PossiblePath> { it.cost }
            .thenComparing(comparingInt<PossiblePath> { it.position.first }.reversed()) // heuristics to reach bottom sooner
            .thenComparing(comparingInt<PossiblePath> { it.position.second }.reversed()) // not sure if useful
            .compare(this, other)
    }
}

private data class PossiblePathKey(
    val direction: Direction,
    val consecutiveSteps: Int,
    val position: YX
)

private operator fun YX.plus(direction: Direction) = YX(this.first + direction.dy, this.second + direction.dx)

private operator fun <T> List<List<T>>.get(yx: YX) = this[yx.first][yx.second]

private operator fun  <T> List<List<T>>.contains(yx: YX) = yx.first in 0..this.lastIndex && yx.second in 0..this[0].lastIndex

private enum class Direction(
    val dy: Int,
    val dx: Int
) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    val turns
        get() = when (this) {
            UP, DOWN -> listOf(LEFT, RIGHT)
            LEFT, RIGHT -> listOf(UP, DOWN)
        }
}