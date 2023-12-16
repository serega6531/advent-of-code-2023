package day10

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day10/input.txt")

    val yx = input.lines()
        .map { it.toCharArray() }
        .map { line -> line.map { Tile.parseTile(it) } }

    val (startY, startX) = yx.indexesOf { it == Tile.START }

    val seq = getLoopSequence(startY, startX, yx)

    val length = seq.count()
    val result = length / 2

    println(result)
}