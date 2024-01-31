package day23

import getResourceAsText

fun main() {
    val field = getResourceAsText("/day23/input.txt").lines()
    val result = solve(field, true)

    println(result)
}

