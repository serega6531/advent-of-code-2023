package day16

import YX

fun getEnergizedNumber(field: List<List<Tile>>, initialState: Triple<Int, Int, Direction>): Int {
    val maxY = field.lastIndex
    val maxX = field.first().lastIndex

    val energized = mutableSetOf<YX>()
    val seenStates = mutableSetOf<Triple<Int, Int, Direction>>()

    val simulateBeamRecursive = DeepRecursiveFunction<Triple<Int, Int, Direction>, Unit> { state ->
        if (state in seenStates) {
            return@DeepRecursiveFunction
        }

        seenStates.add(state)

        val (y, x, direction) = state

        if (y !in 0..maxY || x !in 0..maxX) {
            return@DeepRecursiveFunction
        }

        energized.add(y to x)

        val tile = field[y][x]

        when (tile) {
            Tile.EMPTY -> callRecursive(Triple(y + direction.dy, x + direction.dx, direction))

            Tile.RIGHT_MIRROR -> {
                val (newDirection, newY, newX) = when (direction) {
                    Direction.UP -> Triple(Direction.RIGHT, y, x + 1)
                    Direction.DOWN -> Triple(Direction.LEFT, y, x - 1)
                    Direction.LEFT -> Triple(Direction.DOWN, y + 1, x)
                    Direction.RIGHT -> Triple(Direction.UP, y - 1, x)
                }

                callRecursive(Triple(newY, newX, newDirection))
            }

            Tile.LEFT_MIRROR -> {
                val (newDirection, newY, newX) = when (direction) {
                    Direction.UP -> Triple(Direction.LEFT, y, x - 1)
                    Direction.DOWN -> Triple(Direction.RIGHT, y, x + 1)
                    Direction.LEFT -> Triple(Direction.UP, y - 1, x)
                    Direction.RIGHT -> Triple(Direction.DOWN, y + 1, x)
                }

                callRecursive(Triple(newY, newX, newDirection))
            }

            Tile.VERTICAL_SPLITTER -> {
                when (direction) {
                    Direction.UP, Direction.DOWN -> callRecursive(Triple(y + direction.dy, x, direction))

                    Direction.LEFT, Direction.RIGHT -> {
                        callRecursive(Triple(y - 1, x, Direction.UP))
                        callRecursive(Triple(y + 1, x, Direction.DOWN))
                    }
                }
            }

            Tile.HORIZONTAL_SPLITTER -> {
                when (direction) {
                    Direction.UP, Direction.DOWN -> {
                        callRecursive(Triple(y, x - 1, Direction.LEFT))
                        callRecursive(Triple(y, x + 1, Direction.RIGHT))
                    }

                    Direction.LEFT, Direction.RIGHT -> callRecursive(Triple(y, x + direction.dx, direction))
                }
            }
        }
    }

    simulateBeamRecursive(initialState)
    return energized.size
}

enum class Tile {
    EMPTY,
    RIGHT_MIRROR, LEFT_MIRROR,
    VERTICAL_SPLITTER, HORIZONTAL_SPLITTER;

    companion object {
        fun parse(c: Char): Tile {
            return when (c) {
                '.' -> EMPTY
                '/' -> RIGHT_MIRROR
                '\\' -> LEFT_MIRROR
                '|' -> VERTICAL_SPLITTER
                '-' -> HORIZONTAL_SPLITTER
                else -> throw IllegalArgumentException(c.toString())
            }
        }
    }
}

enum class Direction(
    val dy: Int,
    val dx: Int
) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1)
}