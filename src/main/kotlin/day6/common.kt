package day6

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun solve(time: Long, minDistance: Long): Int {
    val naive = solveNaive(time, minDistance)
    val smart = solveSmart(time, minDistance)
    check(naive == smart) { "Incorrect result" }
    return smart
}

private fun solveNaive(time: Long, minDistance: Long): Int {
    return (1..<time).count { pressTime ->
        val timeLeft = time - pressTime
        val distance = pressTime * timeLeft

        distance > minDistance
    }
}

private fun solveSmart(time: Long, minDistance: Long): Int {
    val (left, right) = solveQuadraticEquation(-1, time, -minDistance).sorted()
    val start = ceil(left).toLong()
    val end = floor(right).toLong()

    return (end - start + 1).toInt()
}

private fun solveQuadraticEquation(a: Long, b: Long, c: Long): Pair<Double, Double> {
    val d = b.toDouble().pow(2) - 4 * a * c
    val sqrtD = sqrt(d)

    val root1 = (-b + sqrtD) / (2*a)
    val root2 = (-b - sqrtD) / (2*a)

    return root1 to root2
}

private fun <T : Comparable<T>> Pair<T,T>.sorted(): Pair<T, T> {
    return Pair(minOf(first, second), maxOf(first, second))
}