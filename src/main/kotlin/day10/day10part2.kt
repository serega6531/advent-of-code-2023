package day10

import YX
import getResourceAsText

fun main() {
    val input = getResourceAsText("/day10/input.txt")

    val yx = input.lines()
        .map { it.toCharArray() }
        .map { line -> line.map { Tile.parseTile(it) } }

    val completeYX = cleanupTiles(yx)
    printGrid(completeYX)

    val result = completeYX.withIndex().sumOf { (y, row) ->
        row.withIndex().count { (x, tile) ->
            if (tile == Tile.GROUND) {
                isEnclosedByLoop(y, x, completeYX)
            } else {
                false
            }
        }
    }

    println(result)
}

private fun cleanupTiles(yx: List<List<Tile>>): List<List<Tile>> {
    val (startY, startX) = yx.indexesOf { it == Tile.START }

    val startingDirections = getStartingDirections(startY, startX, yx)
    val newStartingTile = Tile.entries.find { it.directions == startingDirections.toSet() }!!
    val loop = getLoopSequence(startY, startX, startingDirections.first(), yx).toList()

    return yx.mapIndexed { y, row ->
        row.mapIndexed { x, tile ->
            when {
                tile == Tile.START -> newStartingTile
                YX(y, x) !in loop -> Tile.GROUND
                else -> tile
            }
        }
    }
}

private fun isEnclosedByLoop(y: Int, x: Int, yx: List<List<Tile>>): Boolean {
    val intersections = (0..<x).count { lineX ->
        yx[y][lineX] in setOf(Tile.VERTICAL, Tile.LEFT_DOWN, Tile.RIGHT_DOWN)
    }

    return intersections % 2 == 1
}