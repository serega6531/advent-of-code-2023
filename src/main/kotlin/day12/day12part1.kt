package day12

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day12/input.txt")

    val result = input.lines()
        .sumOf { possiblePermutations(it) }

    println(result)
}

private fun possiblePermutations(line: String): Int {
    val (conditions, groups) = line.split(' ')
    val parsedGroups = groups.split(',').map { it.toInt() }

    val permutations = calculatePermutations(conditions, parsedGroups)
    return permutations
}