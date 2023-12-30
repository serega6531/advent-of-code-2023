package day21

import YX

fun findStart(field: List<String>): YX {
    field.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == 'S') {
                return y to x
            }
        }
    }

    throw IllegalArgumentException()
}

enum class Direction(
    val dy: Int,
    val dx: Int
) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1)
}
