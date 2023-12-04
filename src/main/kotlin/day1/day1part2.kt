package day1

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day1/input.txt")
    val sum2 = part2(input)
    println(sum2)
}

private fun part2(input: String): Int {
    return input.lines()
        .map { line -> line.indices.mapNotNull { start -> findDigit(line, start) } }
        .sumOf { it.first() * 10 + it.last() }
}

private fun findDigit(
    input: String,
    start: Int
): Int? {
    val replacements = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    if (input[start].isDigit()) {
        return input[start].digitToInt()
    }

    val foundWord = replacements.keys.find { word -> input.regionMatches(start, word, 0, word.length) }
    if (foundWord != null) {
        return replacements.getValue(foundWord)
    }

    return null
}