package day12

import getResourceAsText

fun main() { // works in theory, not in practice
    val input = getResourceAsText("/day12/input.txt")

    val result = input.lines()
        .sumOf { possiblePermutations(it) }

    println(result)
}

private fun possiblePermutations(line: String): Int {
    val (conditions, groups) = line.split(' ')
    val parsedGroups = groups.split(',').map { it.toInt() }

    val updatedConditions = List(5) { conditions }.joinToString(separator = "?")
    val updatedGroups = List(5) { parsedGroups }.flatten()

    val permutations = calculatePermutations(updatedConditions, updatedGroups)
    println("$line: $permutations")
    return permutations
}