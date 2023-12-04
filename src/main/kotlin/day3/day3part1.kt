package day3

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day3/input.txt")

    val lines = input.lines()

    val maxHeight = lines.size
    val maxWidth = lines.first().length
    val numberRegex = Regex("\\d+")

    fun isPartNumber(lineIndex: Int, match: MatchResult): Boolean {
        val matchRange = match.range

        val matchStart = matchRange.first
        val matchEnd = matchRange.last

        val topRowMatches = lineIndex > 0 && lines[lineIndex - 1].substringSafe(matchStart - 1, matchEnd + 2).any { it.isSymbol() }
        val sameRowMatchesLeft = matchStart > 0 && lines[lineIndex][matchStart - 1].isSymbol()
        val sameRowMatchesRight = matchEnd < (maxWidth - 1) && lines[lineIndex][matchEnd + 1].isSymbol()
        val bottomRowMatches = lineIndex < (maxHeight - 1) && lines[lineIndex + 1].substringSafe(matchStart - 1, matchEnd + 2).any { it.isSymbol() }

        return topRowMatches || sameRowMatchesLeft || sameRowMatchesRight || bottomRowMatches
    }

    val result = lines.mapIndexed { lineIndex, line ->
        numberRegex.findAll(line)
            .filter { isPartNumber(lineIndex, it) }
            .map { it.value.toInt() }
    }
        .flatMap { it }
        .sum()

    println(result)
}

private fun Char.isSymbol() = !isDigit() && this != '.'

private fun String.substringSafe(start: Int, end: Int): String {
    return substring(start.coerceAtLeast(0), end.coerceAtMost(length))
}
