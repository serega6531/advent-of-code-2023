package day18

import YX
import getResourceAsText

fun main() {
    val dugTiles = getDugTiles()
    val square = getDugTilesSquare(dugTiles)

    // start coordinates hardcoded for this solution :(
    // general solution requires solving point-in-polygon problem first
    val filled = floodFill(258, 2, square)

    val result = filled.sumOf { line -> line.count { it } }

    val asString = filled.toTileString()

    println(result)
}

private fun getDugTiles(): Set<YX> {
    val dugTiles = mutableSetOf<YX>()
    dugTiles.add(0 to 0)

    var currentY = 0
    var currentX = 0

    getResourceAsText("/day18/input.txt").lines()
        .forEach { line ->
            val split = line.split(' ')
            val direction = Direction.parseFromChar(split[0][0])
            val numbers = split[1].toInt()

            repeat(numbers) {
                currentY += direction.dy
                currentX += direction.dx
                dugTiles.add(currentY to currentX)
            }
        }

    return dugTiles
}

private fun getDugTilesSquare(tiles: Set<YX>): List<List<Boolean>> {
    val minY = tiles.minOf { it.first }
    val minX = tiles.minOf { it.second }
    val maxY = tiles.maxOf { it.first }
    val maxX = tiles.maxOf { it.second }

    return List(maxY - minY + 1) { y -> List(maxX - minX + 1) { x -> tiles.contains((y + minY) to (x + minX)) } }
}