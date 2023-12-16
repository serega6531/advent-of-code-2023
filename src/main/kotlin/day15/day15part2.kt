package day15

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day15/input.txt")
    val boxes = List(256) { mutableMapOf<String, Int>() }

    input.split(',')
        .forEach { step ->
            val operation = step.last { !it.isDigit() }
            val label = step.substringBefore(operation)
            val labelHash = label.hash()

            when (operation) {
                '-' -> {
                    boxes[labelHash].remove(label)
                }

                '=' -> {
                    val value = step.substringAfter(operation).toInt()
                    boxes[labelHash][label] = value
                }
            }
        }

    val result = boxes.flatMapIndexed { boxIndex, box ->
        box.entries.mapIndexed { lensIndex, (_, focalLength) ->
            (boxIndex + 1) * (lensIndex + 1) * focalLength
        }
    }.sum()

    println(result)
}

private fun String.hash(): Int {
    var hash = 0

    this.forEach {
        hash = (17 * (hash + it.code)).rem(256)
    }

    return hash
}
