package day6

import getResourceAsText

fun main() {
    val (time, distance) = getResourceAsText("/day6/input.txt")
        .lines()
        .map { it.replace(" ", "") }
        .map { it.substringAfter(":") }
        .map { it.toLong() }

    val result = solve(time, distance)

    println(result)
}