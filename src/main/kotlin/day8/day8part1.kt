package day8

import getResourceAsText

fun main() {
    val (instructions, nodesString) = getResourceAsText("/day8/input.txt").split("\n\n")

    val nodeRegex = Regex("""([A-Z]+) = \(([A-Z]+), ([A-Z]+)\)""")
    val start = "AAA"
    val finish = "ZZZ"

    val nodes = nodesString.lines()
        .map { nodeRegex.matchEntire(it)!! }
        .map { it.destructured }
        .associate { (source, left, right) -> source to (left to right) }

    val result = generateSequence(start to 0) { (current, index) ->
        val leftChoice = instructions[index % instructions.length] == 'L'
        val choices = checkNotNull(nodes[current])
        val next = if (leftChoice) {
            choices.first
        } else {
            choices.second
        }

        next to (index + 1)
    }
        .first { it.first == finish }
        .second

    println(result)
}