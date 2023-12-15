package day13

import getResourceAsText
import transpose
import java.util.ArrayDeque
import java.util.Deque

fun main() {
    val input = getResourceAsText("/day13/input.txt")

    val result = input.split("\n\n")
        .sumOf { processPattern(it) }

    println(result)
}

private fun processPattern(pattern: String): Int {
    val lines = pattern.lines()
    val transposed = lines.transpose()

    val horizontalMirror = getMirror(lines)
    if (horizontalMirror != null) {
        return horizontalMirror * 100
    }

    val verticalMirror = getMirror(transposed)
    if (verticalMirror != null) {
        return verticalMirror
    }

    return 0
}

private fun getMirror(lines: List<String>): Int? {
    val possibleMirrors: MutableMap<Int, Deque<String>> = mutableMapOf()

    lines.forEachIndexed { index: Int, line: String ->
        val new = if (index > 0) {
            val previous = possibleMirrors.getValue(index - 1)
            ArrayDeque(previous).apply { addFirst(line) }
        } else {
            ArrayDeque<String>().apply { addFirst(line) }
        }

        possibleMirrors.values.removeAll { potentialReflection ->
            val reflected = potentialReflection.peekFirst()

            when (reflected) {
                line -> {
                    potentialReflection.removeFirst()
                    false
                }

                null -> false
                else -> true
            }
        }

        if (index != lines.lastIndex) {
            possibleMirrors[index] = new
        }
    }

    check(possibleMirrors.size <= 1)
    return possibleMirrors.keys.firstOrNull()?.plus(1)
}
