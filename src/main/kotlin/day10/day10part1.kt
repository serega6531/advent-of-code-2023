package day10

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day10/input.txt")

    val yx = input.lines()
        .map { it.toCharArray() }
        .map { line -> line.map { it.parseTile() } }

    val maxY = yx.lastIndex
    val maxX = yx.first().lastIndex
    val (startY, startX) = yx.indexesOf { it == Tile.START }

    fun getStartingDirection(): Direction {
        return Direction.entries.find { direction ->
            val nextY = startY + direction.dy
            val nextX = startX + direction.dx

            if (nextX in 0..maxX && nextY in 0..maxY) {
                val nextTile = yx[nextY][nextX]
                val opposite = direction.opposite()

                nextTile.directions.contains(opposite)
            } else {
                false
            }
        }!!
    }

    fun getLoopLength(): Int {
        var lastDirection: Direction = getStartingDirection()
        var currentY = startY + lastDirection.dy
        var currentX = startX + lastDirection.dx
        var length = 1

        while (true) {
            val currentTile = yx[currentY][currentX]

            if (currentTile == Tile.START) {
                return length
            }

            val opposite = lastDirection.opposite()
            lastDirection = (currentTile.directions - opposite).first()

            currentY += lastDirection.dy
            currentX += lastDirection.dx
            length++
        }
    }

    val length = getLoopLength()
    val result = length / 2

    println(result)
}

private fun Char.parseTile(): Tile {
    return when (this) {
        '|' -> Tile.VERTICAL
        '-' -> Tile.HORIZONTAL
        'L' -> Tile.UP_RIGHT
        'J' -> Tile.UP_LEFT
        '7' -> Tile.LEFT_DOWN
        'F' -> Tile.RIGHT_DOWN
        '.' -> Tile.GROUND
        'S' -> Tile.START
        else -> throw IllegalArgumentException(this.toString())
    }
}

private fun List<List<Tile>>.indexesOf(predicate: (Tile) -> Boolean): Pair<Int, Int> {
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (predicate(c)) {
                return y to x
            }
        }
    }

    throw IllegalArgumentException()
}

private enum class Tile(
    val directions: Set<Direction>
) {
    VERTICAL(setOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL(setOf(Direction.LEFT, Direction.RIGHT)),
    UP_RIGHT(setOf(Direction.UP, Direction.RIGHT)),
    UP_LEFT(setOf(Direction.UP, Direction.LEFT)),
    LEFT_DOWN(setOf(Direction.LEFT, Direction.DOWN)),
    RIGHT_DOWN(setOf(Direction.RIGHT, Direction.DOWN)),
    GROUND(emptySet()),
    START(setOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT))
}

private enum class Direction(
    val dy: Int,
    val dx: Int
) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

}