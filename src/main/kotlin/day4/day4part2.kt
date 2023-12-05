package day4

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day4/input.txt")

    val lines = input.lines()
    val cards = MutableList(lines.size) { 1 }

    lines
        .map { it.replace(Regex(" +"), " ") }
        .map{ line -> processLine(line) }
        .forEachIndexed { index, won ->
            if (won > 0) {
                ((index + 1)..(index + won)).forEach {
                    cards[it] += cards[index]
                }
            }
        }

    val result = cards.sum()
    println(result)
}

private fun processLine(line: String): Int {
    val (winning, have) = line.substringAfter(": ")
        .split(" | ")
        .map { it.split(" ") }
        .map { it.map { s -> s.toInt() }.toSet() }

    return (have intersect winning).size
}
