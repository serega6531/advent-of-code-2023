package day10

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day10/example2.txt")

    val yx = input.lines()
        .map { it.toCharArray() }
        .map { line -> line.map { Tile.parseTile(it) } }

    val (startY, startX) = yx.indexesOf { it == Tile.START }

    val loopTiles = getLoopSequence(startY, startX, yx).toList()
    val polygons = getPolygons(loopTiles)

    val result = countPolygonIntersections(loopTiles.toSet(), polygons)

    println(result)
}

private fun countPolygonIntersections(
    loopTiles: Set<YX>,
    polygons: List<Polygon>
): Int {
    val minY = loopTiles.minOf { it.first }
    val maxY = loopTiles.maxOf { it.first }
    val minX = loopTiles.minOf { it.second }
    val maxX = loopTiles.maxOf { it.second }

    val segmentsByNode = createMapOfSegments(polygons)

    val result = (minY..maxY).sumOf { y ->
        (minX..maxX).count { x ->
            val yx = y to x

            if (!loopTiles.contains(yx)) {
                val intersections = countIntersections(y, x, segmentsByNode)
                intersections % 2 == 1
            } else {
                false
            }
        }
    }
    return result
}

private fun getPolygons(loopTiles: List<YX>): List<Polygon> {
    val segments = getPolygonSegments(loopTiles).toMutableList()

    val first = segments.first()
    val last = segments.last()
    val startPoint = last.last()

    if (first[0].first - startPoint.first == first[1].first - first[0].first) {
        val updatedFirst = listOf(startPoint, *first.toTypedArray())
        segments[0] = updatedFirst
    } else {
        val updatedFirst = listOf(startPoint, first.first())
        segments.add(0, updatedFirst)
    }

    if (segments.first()[1].first - segments.first()[0].first == last[1].first - last[0].first) {
        val merged = last + segments.first().drop(1)
        segments[0] = merged
        segments.removeAt(segments.lastIndex)
    }

    return segments
}

private fun getPolygonSegments(loopTiles: List<YX>): Sequence<Polygon> {
    return sequence {
        var polygon = mutableListOf(loopTiles[0], loopTiles[1])
        var dy = loopTiles[1].first - loopTiles[0].first

        loopTiles.drop(2)
            .forEach { tile ->
                val prev = polygon.last()
                val newDy = tile.first - prev.first

                if (newDy != dy) {
                    yield(polygon)
                    polygon = mutableListOf(prev, tile)
                    dy = newDy
                } else {
                    polygon.add(tile)
                }
            }

        yield(polygon)
    }
}

private fun createMapOfSegments(polygons: List<Polygon>): Map<YX, List<Polygon>> {
    val result = mutableMapOf<YX, MutableList<Polygon>>()

    polygons.forEach { polygon ->
        polygon.forEach { yx ->
            result.computeIfAbsent(yx) { mutableListOf() }.add(polygon)
        }
    }

    return result
}

private fun countIntersections(y: Int, x: Int, segmentsByNode: Map<YX, List<Polygon>>): Int {
    val seenTiles = mutableSetOf<YX>()

    return (0..<y).count { intersectionY ->
        val intersectionPoint = intersectionY to x

        val segments = segmentsByNode[intersectionPoint]?.filter { it.isHorizontal() } ?: emptyList()

        if (segments.isNotEmpty()) {
            val seen = seenTiles.contains(intersectionPoint)
            seenTiles.addAll(segments.flatten())
            !seen
        } else {
            false
        }
    }
}

typealias Polygon = List<YX>

private fun Polygon.isHorizontal() = this[0].first == this[1].first