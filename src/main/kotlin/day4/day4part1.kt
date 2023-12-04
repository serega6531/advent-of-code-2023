package day4

import getResourceAsText
import kotlin.math.pow

fun main() {
    val input = getResourceAsText("/day4/input.txt")

    val result = input.lines()
        .map { it.replace(Regex(" +"), " ") }
        .sumOf { processLine(it) }

    println(result)
}

private fun processLine(line: String): Int {
    val (winning, have) = line.substringAfter(": ")
        .split(" | ")
        .map { it.split(" ") }
        .map { it.map { s -> s.toInt() }.toSet() }

    val won = (have intersect winning).size
    return 2.0.pow((won - 1).toDouble()).toInt()
}
