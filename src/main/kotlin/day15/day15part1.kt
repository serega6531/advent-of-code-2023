package day15

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day15/input.txt")

    val result = input.split(',')
        .sumOf { it.hash() }

    println(result)
}

private fun String.hash(): Int {
    var hash = 0

    this.forEach {
        hash = (17 * (hash + it.code)).rem(256)
    }

    return hash
}
