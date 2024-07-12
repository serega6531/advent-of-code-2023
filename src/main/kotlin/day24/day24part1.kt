package day24

import getResourceAsText

fun main() {
    val stones = getResourceAsText("/day24/input.txt").lines()
        .map { line ->
            line.split(" @ ")
                .map { it.split(",") }
                .flatten()
                .map { it.trim().toLong() }
        }
        .map { PositionAndVelocity(it[0], it[1], it[2], it[3], it[4], it[5]) }

    val testAreaStart = 200000000000000.0
    val testAreaEnd = 400000000000000.0

    val result = stones.combinationsWithoutRepeats()
        .count { (stone1, stone2) ->
            solve(stone1, stone2, testAreaStart, testAreaEnd)
        }

    println(result)
}

private fun <T> List<T>.combinationsWithoutRepeats(): List<Pair<T, T>> {
    return this.flatMapIndexed { index, item1 ->
        this.subList(index + 1, this.size).map { item2 ->
            Pair(item1, item2)
        }
    }
}

/**
 * https://stackoverflow.com/q/2931573
 */
private fun solve(s1: PositionAndVelocity, s2: PositionAndVelocity, rangeStart: Double, rangeEnd: Double): Boolean {
    val dx = s2.px - s1.px
    val dy = s2.py - s1.py

    val det = s2.vx * s1.vy - s2.vy * s1.vx

    if (det == 0L) {
        // did not intersect or rays are equal
        return false
    }

    val u = (dy * s2.vx - dx * s2.vy).toDouble() / det
    val v = (dy * s1.vx - dx * s1.vy).toDouble() / det

    if (u < 0 || v < 0) {
        // crossed in the past
        return false
    }

    val p = Vector2(
        x = s1.px + s1.vx * u,
        y = s1.py + s1.vy * u
    )

    if (p.x < rangeStart || p.x > rangeEnd || p.y < rangeStart || p.y > rangeEnd) {
        // outside of test area
        return false
    }

    return true
}

private data class Vector2(
    val x: Double,
    val y: Double
)