package day16

import getResourceAsText

fun main() {
    val field = getResourceAsText("/day16/input.txt")
        .lines()
        .map { line -> line.map { Tile.parse(it) } }

    val maxY = field.lastIndex
    val maxX = field.first().lastIndex

    val starts =
        (0..maxX).flatMap {
            listOf(
                Triple(0, it, Direction.DOWN),
                Triple(maxY, it, Direction.UP)
            )
        } + (0..maxY).flatMap {
            listOf(
                Triple(it, 0, Direction.RIGHT),
                Triple(it, maxX, Direction.LEFT)
            )
        }

    val result = starts.maxOf { start ->
        getEnergizedNumber(field, start)
    }

    println(result)
}

