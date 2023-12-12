package day11

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day11/input.txt")

    val lines = input.lines()
    val result = solve(lines, expanseSpeed = 1000000)

    println(result)
}