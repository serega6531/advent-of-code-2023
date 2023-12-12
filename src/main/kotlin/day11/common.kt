package day11

import kotlin.math.abs

fun solve(lines: List<String>, expanseSpeed: Int): Long {
    val maxY = lines.lastIndex
    val maxX = lines.first().lastIndex

    val galaxiesXY: Array<BooleanArray> = Array(maxY + 1) { y -> BooleanArray(maxX + 1) { x -> lines[y][x] == '#' } }
    val emptyLines = Array(maxY + 1) { y -> galaxiesXY[y].all { !it } }
    val emptyColumns = Array(maxX + 1) { x -> galaxiesXY.all { !it[x] } }

    val lineOffsets = calculateOffsets(emptyLines, expanseSpeed)
    val columnOffsets = calculateOffsets(emptyColumns, expanseSpeed)

    val galaxies: List<YX> = (0..maxY).cartesianProduct(0..maxX)
        .filter { (y, x) -> galaxiesXY[y][x] }

    val result = galaxies.permutations { pair1, pair2 ->
        val lineOffset1 = lineOffsets[pair1.first]
        val columnOffset1 = columnOffsets[pair1.second]

        val lineOffset2 = lineOffsets[pair2.first]
        val columnOffset2 = columnOffsets[pair2.second]

        val dy = abs(lineOffset1 - lineOffset2)
        val dx = abs(columnOffset1 - columnOffset2)

        dy + dx
    }.sumOf { it.toLong() }
    return result
}

private fun calculateOffsets(presence: Array<Boolean>, expanseSpeed: Int) = presence.runningFold(0) { acc, empty ->
    if (empty) {
        acc + expanseSpeed
    } else {
        acc + 1
    }
}

private fun <T, U> Iterable<T>.cartesianProduct(c2: Iterable<U>): List<Pair<T, U>> {
    return this.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}

private fun <T, R> List<T>.permutations(action: (T, T) -> R): List<R> {
    return this.flatMapIndexed { index, first ->
        this.subList(index + 1, this.size).map { second ->
            action(first, second)
        }
    }
}

private typealias YX = Pair<Int, Int>