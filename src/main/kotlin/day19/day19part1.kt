package day19

import getResourceAsText

fun main() {
    val (workflowsString, partsString) = getResourceAsText("/day19/input.txt").split("\n\n")

    val workflows = workflowsString.lines()
        .map { parseWorkflow(it) }
        .associate { it.name to it.rules }

    val parts = partsString.lines()
        .map { parsePart(it) }

    val result = parts.sumOf { solve(it, workflows) }
    println(result)
}

private fun solve(part: Part, workflows: Map<String, List<Rule>>): Int {
    return if (isAccepted(part, workflows)) {
        part.x + part.m + part.a + part.s
    } else {
        0
    }
}

private fun isAccepted(part: Part, workflows: Map<String, List<Rule>>): Boolean {
    var currentRules = workflows.getValue("in")

    while (true) {
        val toFollow = currentRules.first {
            when (it) {
                is ImmediateActionRule -> true
                is ConditionRule -> it.test(part)
            }
        }

        when (val action = toFollow.action) {
            is AcceptAction -> return true
            is RejectAction -> return false
            is RedirectAction -> currentRules = workflows.getValue(action.workflow)
        }
    }
}
