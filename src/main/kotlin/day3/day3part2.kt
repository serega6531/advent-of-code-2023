package day3

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day3/input.txt")

    val lines = input.lines()

    val maxHeight = lines.size
    val maxWidth = lines.first().length
    val numberRegex = Regex("\\d+")

    val gears = mutableMapOf<Pair<Int, Int>, List<Int>>()

    fun checkGear(lineIndex: Int, columnIndex: Int, partNumber: Int) {
        if (lineIndex !in 0..<maxHeight) {
            return
        }

        if (columnIndex !in 0..<maxWidth) {
            return
        }

        if (lines[lineIndex][columnIndex].isGear()) {
            gears.merge(lineIndex to columnIndex, listOf(partNumber)) { old, new -> old + new }
        }
    }

    fun processPartNumber(lineIndex: Int, match: MatchResult) {
        val matchRange = match.range

        val matchStart = matchRange.first
        val matchEnd = matchRange.last
        val partNumber = match.value.toInt()

        val row = IntRange(matchStart - 1, matchEnd + 1)

        row.forEach { index ->
            checkGear(lineIndex - 1, index, partNumber)
            checkGear(lineIndex + 1, index, partNumber)
        }

        checkGear(lineIndex, matchStart - 1, partNumber)
        checkGear(lineIndex, matchEnd + 1, partNumber)
    }

    lines.forEachIndexed { lineIndex, line ->
        numberRegex.findAll(line)
            .forEach { processPartNumber(lineIndex, it) }
    }

    val result = gears.values
        .filter { numbers -> numbers.size == 2 }
        .sumOf { numbers -> numbers.first() * numbers.last() }

    println(result)
}

private fun Char.isGear() = this == '*'
