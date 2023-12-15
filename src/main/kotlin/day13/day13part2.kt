package day13

import getResourceAsText
import java.util.ArrayDeque
import java.util.Deque
import kotlin.math.abs

fun main() {
    val input = getResourceAsText("/day13/example.txt")

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
    val smudgeFixedAt: MutableSet<Int> = mutableSetOf()

    lines.forEachIndexed { index: Int, line: String ->
        val new = if (index > 0) {
            val previous = possibleMirrors.getValue(index - 1)
            ArrayDeque(previous).apply { addFirst(line) }
        } else {
            ArrayDeque<String>().apply { addFirst(line) }
        }

        possibleMirrors.entries.removeAll { (reflectionStart, potentialReflection) ->
            val reflected = potentialReflection.peekFirst()

            when {
                reflected == null -> false

                reflected.equalWithSmudge(line) && reflectionStart !in smudgeFixedAt -> {
                    potentialReflection.removeFirst()
                    smudgeFixedAt.add(reflectionStart)
                    false
                }

                reflected == line -> false

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

private fun String.equalWithSmudge(s2: String): Boolean {
    val rocks1 = this.rocksIndexes()
    val rocks2 = s2.rocksIndexes()

    if (abs(rocks1.size - rocks2.size) != 1) {
        return false
    }

    val combined = setOf(*rocks1.toTypedArray(), *rocks2.toTypedArray())
    return combined.size == maxOf(rocks1.size, rocks2.size)
}

private fun String.rocksIndexes() =
    this.withIndex().filter { (index, value) -> value == '#' }.map { (index, _) -> index }

private fun List<String>.transpose(): List<String> =
    buildList {
        this@transpose.first().indices.forEach { index ->
            val sb = StringBuilder()

            this@transpose.forEach { line ->
                sb.append(line[index])
            }

            add(sb.toString())
        }
    }