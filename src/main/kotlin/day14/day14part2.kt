package day14

import getResourceAsText
import transpose

fun main() {
    val input = getResourceAsText("/day14/input.txt").lines()

    val target = 1000000000
    val (cycleStart, cyclePeriod) = findCycleParameters(input)
    val minimalMatching = getMinimalMatching(target, cycleStart, cyclePeriod)

    val cycled = getSequence(input)
        .first { (_, index) -> index == minimalMatching }
        .first

    val result = cycled
        .reversed()
        .withIndex()
        .sumOf { (index, value) -> (index + 1) * value.count { it == 'O' } }

    println(result)
}

private fun getMinimalMatching(target: Int, start: Int, period: Int): Int {
    var minimalMatching = target - (period * (target / period))

    while (minimalMatching < start) {
        minimalMatching += period
    }

    return minimalMatching
}

private fun getSequence(input: List<String>): Sequence<Pair<List<String>, Int>> {
    return generateSequence(input to 0) { (value, index) -> cycle(value) to (index + 1) }
}

private fun findCycleParameters(input: List<String>): Pair<Int, Int> {
    val states = mutableMapOf<List<String>, Int>()

    return getSequence(input)
        .firstNotNullOf { (state, index) ->
            val found = states[state]

            if (found == null) {
                states[state] = index
            }

            if (found != null) {
                found to (index - found)
            } else {
                null
            }
        }
}

private fun cycle(input: List<String>): List<String> {
    val shiftedNorth = input
        .transpose()
        .map { it.shiftLeft() }
        .transpose()

    val shiftedWest = shiftedNorth.map { it.shiftLeft() }

    val shiftedSouth = shiftedWest
        .transpose()
        .map { it.reversed() }
        .map { it.shiftLeft() }
        .transpose()
        .reversed()

    val shiftedEast = shiftedSouth
        .map { it.reversed() }
        .map { it.shiftLeft() }
        .map { it.reversed() }

    return shiftedEast
}


