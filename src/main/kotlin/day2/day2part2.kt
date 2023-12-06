package day2

import getResourceAsText
import product

fun main() {
    val input = getResourceAsText("/day2/input.txt")

    val result = input.lines()
        .sumOf { processLine(it) }

    println(result)
}

private fun processLine(line: String): Int {
    val subsets = line.split(": ")[1].split("; ")

    val product = subsets
        .map { processSubset(it) }
        .fold(mutableMapOf<String, Int>()) { current, new -> updateMax(current, new) }
        .values
        .product()

    return product
}

private fun processSubset(subset: String): Map<String, Int> {
    return subset.split(", ").associate { processColorDescription(it) }
}

private fun processColorDescription(description: String): Pair<String, Int> {
    val split = description.split(" ")
    return split[1] to split[0].toInt()
}

private fun updateMax(current: MutableMap<String, Int>, new: Map<String, Int>): MutableMap<String, Int> {
    new.forEach { (k, v) -> current.merge(k, v) { old, new -> maxOf(old, new) } }
    return current
}
