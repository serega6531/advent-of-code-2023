package day10

fun getStartingDirection(startY: Int, startX: Int, yx: List<List<Tile>>): Direction {
    val maxY = yx.lastIndex
    val maxX = yx.first().lastIndex

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

fun getLoopSequence(startY: Int, startX: Int, yx: List<List<Tile>>): Sequence<Pair<Int, Int>> {
    return sequence {
        var lastDirection: Direction = getStartingDirection(startY, startX, yx)
        var currentY = startY + lastDirection.dy
        var currentX = startX + lastDirection.dx

        while (true) {
            val currentTile = yx[currentY][currentX]
            yield(currentY to currentX)

            if (currentTile == Tile.START) {
                return@sequence
            }

            val opposite = lastDirection.opposite()
            lastDirection = (currentTile.directions - opposite).first()

            currentY += lastDirection.dy
            currentX += lastDirection.dx
        }
    }
}

fun List<List<Tile>>.indexesOf(predicate: (Tile) -> Boolean): Pair<Int, Int> {
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (predicate(c)) {
                return y to x
            }
        }
    }

    throw IllegalArgumentException()
}

enum class Tile(
    val directions: Set<Direction>
) {

    VERTICAL(setOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL(setOf(Direction.LEFT, Direction.RIGHT)),
    UP_RIGHT(setOf(Direction.UP, Direction.RIGHT)),
    UP_LEFT(setOf(Direction.UP, Direction.LEFT)),
    LEFT_DOWN(setOf(Direction.LEFT, Direction.DOWN)),
    RIGHT_DOWN(setOf(Direction.RIGHT, Direction.DOWN)),
    GROUND(emptySet()),
    START(setOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));

    companion object {
        fun parseTile(char: Char): Tile {
            return when (char) {
                '|' -> VERTICAL
                '-' -> HORIZONTAL
                'L' -> UP_RIGHT
                'J' -> UP_LEFT
                '7' -> LEFT_DOWN
                'F' -> RIGHT_DOWN
                '.' -> GROUND
                'S' -> START
                else -> throw IllegalArgumentException(this.toString())
            }
        }
    }
}

enum class Direction(
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