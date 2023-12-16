package day14

import getResourceAsText
import transpose

fun main() {
    val input = getResourceAsText("/day14/input.txt").lines()
    val transposed = input.transpose()

    val result = transposed.map { it.shiftLeft() }
        .transpose()
        .reversed()
        .withIndex()
        .sumOf { (index, value) -> (index + 1) * value.count { it == 'O' } }

    println(result)
}
