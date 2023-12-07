package day6

import getResourceAsText
import product

fun main() {
    val (times, distances) = getResourceAsText("/day6/input.txt")
        .lines()
        .map { it.replace(Regex(" +"), " ") }
        .map { it.substringAfter(": ") }
        .map { it.split(' ') }
        .map { it.map { s -> s.toLong() } }

    val result = times.zip(distances)
        .map { (time, distance) -> solve(time, distance) }
        .product()

    println(result)
}