package day18

import YX

fun floodFill(startY: Int, startX: Int, toFill: List<List<Boolean>>): List<List<Boolean>> {
    val result = toFill.map { it.toMutableList() }.toMutableList()
    val maxY = toFill.lastIndex
    val maxX = toFill.first().lastIndex

    val floodFillStep = DeepRecursiveFunction<YX, Unit> { (y, x) ->
        if (y !in 0..maxY || x !in 0..maxX) {
            return@DeepRecursiveFunction
        }

        if (result[y][x]) {
            return@DeepRecursiveFunction
        }

        result[y][x] = true

        Direction.entries.forEach { direction ->
            callRecursive(Pair(y + direction.dy, x + direction.dx))
        }
    }

    floodFillStep(startY to startX)

    return result
}

fun List<List<Boolean>>.toTileString(): String {
    return this.joinToString(separator = "\n") {
        it.map { b -> if (b) '#' else '.' }.joinToString(separator = "")
    }
}

enum class Direction(
    val dy: Int,
    val dx: Int
) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    companion object {
        fun parseFromChar(c: Char) = when (c) {
            'U' -> UP
            'D' -> DOWN
            'L' -> LEFT
            'R' -> RIGHT
            else -> throw IllegalArgumentException(c.toString())
        }

        fun parseFromColorChar(c: Char) = when (c) {
            '3' -> UP
            '1' -> DOWN
            '2' -> LEFT
            '0' -> RIGHT
            else -> throw IllegalArgumentException(c.toString())
        }
    }
}