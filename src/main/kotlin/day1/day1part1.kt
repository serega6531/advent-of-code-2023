package day1

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day1/input.txt")
    val sum1 = part1(input)
    println(sum1)
}

private fun part1(input: String): Int {
    return input.lines()
        .map { it.replace(Regex("\\D"), "") }
        .sumOf { it.first().digitToInt() * 10 + it.last().digitToInt() }
}