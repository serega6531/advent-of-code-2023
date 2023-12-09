package day9

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day9/input.txt")

    val result = input.lines()
        .sumOf { processLine(it) }

    println(result)
}

private fun processLine(line: String): Int {
    val history = line.split(' ').map { it.toInt() }
    return getNext(history)
}

private fun getNext(values: List<Int>): Int {
    if (values.all { it == 0 }) {
        return 0
    }

    val diffs = values.zipWithNext { a, b -> b - a }
    return values.last() + getNext(diffs)
}
