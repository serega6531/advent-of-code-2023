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

    val permutations = possiblePermutations0(conditions, parsedGroups)
    return permutations
}

private fun possiblePermutations0(conditions: String, groups: List<Int>): Int {
    if (conditions.none { it == '?' }) {
        return if (isCorrectPermutation(conditions, groups)) {
            1
        } else {
            0
        }
    }

    val firstUnknown = conditions.indexOfFirst { it == '?' }
    val withOperational = possiblePermutations0(conditions.with(firstUnknown, '.'), groups)
    val withDamaged = possiblePermutations0(conditions.with(firstUnknown, '#'), groups)

    return withOperational + withDamaged
}

private fun isCorrectPermutation(conditions: String, groups: List<Int>): Boolean {
    val actualGroups = conditions.split(Regex("\\.+")).map { it.count() }.filter { it != 0 }
    return groups == actualGroups
}

private fun String.with(index: Int, c: Char): String {
    return this.toCharArray().also { it[index] = c }.concatToString()
}