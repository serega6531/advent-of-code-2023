package day19

import getResourceAsText
import java.util.function.Predicate

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

private fun parseWorkflow(s: String): Workflow {
    val name = s.substringBefore('{')
    val rules = s.substringBetween('{', '}')
        .split(',')
        .map { parseRule(it) }

    return Workflow(name, rules)
}

private fun parseRule(s: String): Rule {
    return if (s.contains(':')) {
        val spl = s.split(":")
        val component = spl[0][0]
        val condition = parseCondition(spl[0][1])
        val value = spl[0].substring(2).toInt()
        val action = parseAction(spl[1])

        ConditionRule(component, condition, value, action)
    } else {
        ImmediateActionRule(parseAction(s))
    }
}

private fun parseAction(s: String): Action {
    return when (s) {
        "A" -> AcceptAction
        "R" -> RejectAction
        else -> RedirectAction(s)
    }
}

private fun parseCondition(c: Char): Condition {
    return when (c) {
        '>' -> Condition.GREATER_THAN
        '<' -> Condition.LESS_THAN
        else -> throw IllegalArgumentException(c.toString())
    }
}

private fun parsePart(s: String): Part {
    val ratings = s.substringBetween('{', '}')
        .split(',')
        .map { it.split('=') }
        .associate { (rating, value) -> rating to value.toInt() }

    return Part(
        x = ratings.getValue("x"),
        m = ratings.getValue("m"),
        a = ratings.getValue("a"),
        s = ratings.getValue("s")
    )
}

private data class Workflow(val name: String, val rules: List<Rule>)

private sealed interface Rule {
    val action: Action
}

private data class ConditionRule(
    val component: Char,
    val condition: Condition,
    val value: Int,
    override val action: Action
) :
    Rule, Predicate<Part> {

    override fun test(part: Part): Boolean {
        val actualValue = when (component) {
            'x' -> part.x
            'm' -> part.m
            'a' -> part.a
            's' -> part.s
            else -> throw IllegalStateException()
        }

        return when (condition) {
            Condition.GREATER_THAN -> actualValue > value
            Condition.LESS_THAN -> actualValue < value
        }
    }
}

private data class ImmediateActionRule(override val action: Action) : Rule

private sealed interface Action

private data object AcceptAction : Action

private data object RejectAction : Action

private data class RedirectAction(val workflow: String) : Action

private data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
)

private enum class Condition { GREATER_THAN, LESS_THAN }

private fun String.substringBetween(start: Char, finish: Char): String {
    return this.substringAfter(start).substringBefore(finish)
}
