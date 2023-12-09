package day9

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day9/input.txt")

    val result = input.lines()
        .sumOf { processLine(it) }

    println(result)
}

private fun processLine(line: String): Int {
    val history = line.split(' ').map { it.toInt() }.asReversed()
    val next = getNext(history)
    return next
}
