package day16

import getResourceAsText

fun main() {
    val field = getResourceAsText("/day16/input.txt")
        .lines()
        .map { line -> line.map { Tile.parse(it) } }

    val result = getEnergizedNumber(field, Triple(0, 0, Direction.RIGHT))
    println(result)
}

