package day23

import YX

fun solve(field: List<String>, ignoreArrows: Boolean): Int {
    val maxY = field.lastIndex
    val maxX = field.first().lastIndex

    val startY = 0
    val startX = 1
    val endY = maxY
    val endX = maxX - 1

    val findLongestPath = DeepRecursiveFunction<Triple<Int, Int, Set<YX>>, Set<YX>?> { state ->
        val (y, x, path) = state

        if (y == endY && x == endX) {
            println(path.size)
            path
        } else {
            Direction.entries
                .associateWith { direction -> (y + direction.dy) to (x + direction.dx) }
                .filterValues { (newY, newX) -> newY in 0..maxY && newX in 0..maxX }
                .filter { (direction, newYX) ->
                    val (newY, newX) = newYX
                    when (val tile = field[newY][newX]) {
                        '.' -> true
                        '^', 'v', '<', '>' -> ignoreArrows || tile == direction.char
                        else -> false
                    }
                }
                .values
                .filter { newYX -> newYX !in path }
                .mapNotNull { newYX -> callRecursive(Triple(newYX.first, newYX.second, path + newYX)) }
                .maxByOrNull { it.size }
        }
    }

    val path = findLongestPath(Triple(startY, startX, setOf(startY to startX)))!!
    val steps = path.size - 1

    return steps
}

private enum class Direction(
    val dy: Int,
    val dx: Int,
    val char: Char
) {
    UP(-1, 0, '^'), DOWN(1, 0, 'v'), LEFT(0, -1, '<'), RIGHT(0, 1, '>')
}