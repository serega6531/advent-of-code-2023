package day19

import java.util.function.Predicate

fun parseWorkflow(s: String): Workflow {
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

fun parsePart(s: String): Part {
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

data class Workflow(val name: String, val rules: List<Rule>)

sealed interface Rule {
    val action: Action
}

data class ConditionRule(
    val component: Char,
    val condition: Condition,
    val value: Int,
    override val action: Action
) :
    Rule, Predicate<Part> {

    override fun test(part: Part): Boolean {
        val actualValue = part.byName(component)

        return when (condition) {
            Condition.GREATER_THAN -> actualValue > value
            Condition.LESS_THAN -> actualValue < value
        }
    }
}

data class ImmediateActionRule(override val action: Action) : Rule

sealed interface Action

data object AcceptAction : Action

data object RejectAction : Action

data class RedirectAction(val workflow: String) : Action

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    fun byName(c: Char): Int {
        return when (c) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException(c.toString())
        }
    }
}

enum class Condition { GREATER_THAN, LESS_THAN }

private fun String.substringBetween(start: Char, finish: Char): String {
    return this.substringAfter(start).substringBefore(finish)
}
