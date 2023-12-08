package day8

fun getNodeSequence(
    start: String,
    instructions: String,
    nodes: Map<String, Pair<String, String>>
) = generateSequence(start to 0L) { (current, index) ->
    val leftChoice = instructions[(index % instructions.length).toInt()] == 'L'
    val choices = checkNotNull(nodes[current])
    val next = if (leftChoice) {
        choices.first
    } else {
        choices.second
    }

    next to (index + 1)
}