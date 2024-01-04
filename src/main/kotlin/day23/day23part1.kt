package day23

import YX
import getResourceAsText

fun main() {
    val field = getResourceAsText("/day23/input.txt").lines()
    val maxY = field.lastIndex
    val maxX = field.first().lastIndex

    val endY = maxY
    val endX =  maxX - 1

    fun findLongestPath(y: Int, x: Int, path: Set<YX> = setOf(y to x)): Set<YX>? {
        if (y == endY && x == endX) {
            return path
        }

        return Direction.entries
            .associateWith { direction -> (y + direction.dy) to (x + direction.dx) }
            .filterValues { (newY, newX) -> newY in 0..maxY && newX in 0..maxX }
            .filter { (direction, newYX) ->
                val (newY, newX) = newYX
                when (val tile = field[newY][newX]) {
                    '.' -> true
                    '^', 'v', '<', '>' -> tile == direction.char
                    else -> false
                }
            }
            .values
            .filter { (newY, newX) -> (newY to newX) !in path }
            .mapNotNull { newYX -> findLongestPath(newYX.first, newYX.second, path + newYX) }
            .toList()
            .maxByOrNull { it.size }
    }

    val path = findLongestPath(0, 1)!!
    val steps = path.size - 1
    println(steps)
}

private enum class Direction(
    val dy: Int,
    val dx: Int,
    val char: Char
) {
    UP(-1, 0, '^'), DOWN(1, 0, 'v'), LEFT(0, -1, '<'), RIGHT(0, 1, '>')
}