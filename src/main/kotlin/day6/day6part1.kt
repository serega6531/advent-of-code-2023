package day6

import getResourceAsText
import product

fun main() {
    val (times, distances) = getResourceAsText("/day6/input.txt")
        .lines()
        .map { it.replace(Regex(" +"), " ") }
        .map { it.substringAfter(": ") }
        .map { it.split(' ') }
        .map { it.map { s -> s.toInt() } }

    val result = times.zip(distances)
        .map { (time, distance) -> solve(time, distance) }
        .product()

    println(result)
}

private fun solve(time: Int, minDistance: Int): Int {
    return (1..<time).count { pressTime ->
        val timeLeft = time - pressTime
        val distance = pressTime * timeLeft

        distance > minDistance
    }
}