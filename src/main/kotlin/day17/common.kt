package day17

import YX
import java.util.*
import java.util.Comparator.comparingInt

fun solve(field: List<List<Int>>, nextDirectionsGenerator: (path: PossiblePath) -> List<Direction>, nextPathsFilter: (PossiblePath) -> Boolean): Int {
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

        val nextPaths = nextPaths(path, field, nextDirectionsGenerator)
            .filter(nextPathsFilter)
            .filter { nextPath -> nextPath.cost.lessThanIgnoringNull(minCosts[nextPath.toKey()]) }

        minCosts.putAll(nextPaths.associate { it.toKey() to it.cost })
        queue.addAll(nextPaths)
    }

    throw IllegalStateException()
}

private fun nextPaths(path: PossiblePath, field: List<List<Int>>, nextDirectionsGenerator: (path: PossiblePath) -> List<Direction>): List<PossiblePath> {
    val nextDirections = nextDirectionsGenerator(path)

    return nextDirections
        .filter { newDirection -> (path.position + newDirection) in field }
        .map { newDirection ->
            PossiblePath(
                direction = newDirection,
                consecutiveSteps = if (newDirection == path.direction) path.consecutiveSteps + 1 else 1,
                cost = path.cost + field[path.position + newDirection],
                position = path.position + newDirection
            )
        }
}

private fun Int.lessThanIgnoringNull(other: Int?) = other == null || this < other

data class PossiblePath(
    val direction: Direction,
    val consecutiveSteps: Int,
    val cost: Int,
    val position: YX
) : Comparable<PossiblePath> {

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

data class PossiblePathKey(
    val direction: Direction,
    val consecutiveSteps: Int,
    val position: YX
)

operator fun YX.plus(direction: Direction) = YX(this.first + direction.dy, this.second + direction.dx)

operator fun <T> List<List<T>>.get(yx: YX) = this[yx.first][yx.second]

operator fun  <T> List<List<T>>.contains(yx: YX) = yx.first in 0..this.lastIndex && yx.second in 0..this[0].lastIndex

enum class Direction(
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