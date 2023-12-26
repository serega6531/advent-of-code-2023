package day18

import getResourceAsText
import java.io.FileWriter
import java.util.TreeMap

fun main() {
//    val dugTiles = getDugTiles()
//    val square = getDugTilesSquare(dugTiles)

    val dugTilesRanges = getDugTilesRanges()
    val square = writeDugTilesSquare(dugTilesRanges, FileWriter("square.txt"))

    // start coordinates hardcoded for this solution :(
    // general solution requires solving point-in-polygon problem first
//    val filled = floodFill(258, 2, square)

//    val result = filled.sumOf { line -> line.count { it } }

//    val asString = square.toTileString()

//    println(asString)
    println()
}

private fun getDugTilesRanges(): Map<Pair<DirectionType, Int>, TreeMap<Int, Int>> {
    val ranges = mutableMapOf<Pair<DirectionType, Int>, TreeMap<Int, Int>>()

    var currentY = 0
    var currentX = 0

    getResourceAsText("/day18/example.txt").lines()
        .forEach { line ->
            val color = line.split(' ').last().substringAfter("(#").substringBefore(")")
            val number = color.dropLast(1).toInt(16)
            val direction = Direction.parseFromColorChar(color.last())

            val startY = currentY
            val endY = currentY + direction.dy * number

            val startX = currentX
            val endX = currentX + direction.dx * number

            when (direction) {
                Direction.UP, Direction.DOWN -> {
                    val (lowerY, higherY) = listOf(startY, endY).sorted()

                    val columnRanges = ranges.computeIfAbsent(DirectionType.VERTICAL to startX) { TreeMap() }
                    columnRanges[lowerY] = higherY
                }

                Direction.LEFT, Direction.RIGHT -> {
                    val (lowerX, higherX) = listOf(startX, endX).sorted()

                    val lineRanges = ranges.computeIfAbsent(DirectionType.HORIZONTAL to startX) { TreeMap() }
                    lineRanges[lowerX] = higherX
                }
            }

            currentY = endY
            currentX = endX
        }

    return ranges
}

private fun writeDugTilesSquare(ranges: Map<Pair<DirectionType, Int>, TreeMap<Int, Int>>, out: FileWriter) {
    val minY = ranges.keys.filter { (type, _) -> type == DirectionType.HORIZONTAL }.minOf { (_, y) -> y }
    val maxY = ranges.keys.filter { (type, _) -> type == DirectionType.HORIZONTAL }.maxOf { (_, y) -> y }
    val minX = ranges.keys.filter { (type, _) -> type == DirectionType.VERTICAL }.minOf { (_, x) -> x }
    val maxX = ranges.keys.filter { (type, _) -> type == DirectionType.VERTICAL }.maxOf { (_, x) -> x }

    return repeat(maxY - minY + 1) { y ->
        repeat(maxX - minX + 1) { x ->
            val originalY = y + minY
            val originalX = x + minX

            val verticalRanges = ranges[DirectionType.VERTICAL to originalX] ?: TreeMap()
            val horizontalRanges = ranges[DirectionType.VERTICAL to originalY] ?: TreeMap()

            val verticalRange = verticalRanges.floorEntry(originalY)
            val horizontalRange = horizontalRanges.floorEntry(originalX)

            val inRange = (verticalRange != null && originalY in verticalRange.toRange()) || (horizontalRange != null && originalX in horizontalRange.toRange())
            val c = if (inRange) '#' else '.'
            out.write(c.toString())
        }

        out.write("\n")
    }
}

private fun Map.Entry<Int, Int>.toRange() = IntRange(key, value)

private enum class DirectionType {
    HORIZONTAL, VERTICAL
}
