package day2

import getResourceAsText

private val maxRed = 12
private val maxGreen = 13
private val maxBlue = 14

fun main() {
    val input = getResourceAsText("/day2/input.txt")

    val result = input.lines()
        .map { processLine(it) }
        .filter { it.possible }
        .sumOf { it.id }

    println(result)
}

private fun processLine(line: String): GameResult {
    val gameId = Regex("^Game (\\d+): .+$").matchEntire(line)!!.groupValues[1].toInt()
    val subsets = line.split(": ")[1].split("; ")

    val possible = subsets.all { processSubset(it) }
    return GameResult(gameId, possible)
}

private fun processSubset(subset: String): Boolean {
    val colors = subset.split(", ").associate { processColorDescription(it) }
    val red = colors["red"] ?: 0
    val green = colors["green"] ?: 0
    val blue = colors["blue"] ?: 0

    return red <= maxRed && green <= maxGreen && blue <= maxBlue
}

private fun processColorDescription(description: String): Pair<String, Int> {
    val split = description.split(" ")
    return split[1] to split[0].toInt()
}

private data class GameResult(
    val id: Int,
    val possible: Boolean
)
