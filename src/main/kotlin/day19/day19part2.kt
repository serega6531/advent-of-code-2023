package day19

import getResourceAsText

fun main() {
    val workflowsString = getResourceAsText("/day19/input.txt").split("\n\n")[0]

    val workflows = workflowsString.lines()
        .map { parseWorkflow(it) }
        .associate { it.name to it.rules }

    val results = mutableSetOf<PossibleRanges>()

    fun possibleCombinations(
        ranges: PossibleRanges,
        remainingRules: List<Rule>
    ) {
        val rule = remainingRules[0]
        val action = rule.action

        when (rule) {
            is ImmediateActionRule -> {
                when (action) {
                    AcceptAction -> results.add(ranges)
                    RejectAction -> return
                    is RedirectAction -> possibleCombinations(ranges, workflows.getValue(action.workflow))
                }
            }

            is ConditionRule -> {
                val component = rule.component
                val condition = rule.condition
                val conditionValue = rule.value

                val currentRange = ranges.byName(component)

                val (matches, doesNotMatch) = when (condition) {
                    Condition.GREATER_THAN -> currentRange.splitGreaterThan(conditionValue)
                    Condition.LESS_THAN -> currentRange.splitLessThan(conditionValue)
                }

                if (matches != null) {
                    val newRanges = ranges.replaceByName(component, matches)

                    when (action) {
                        AcceptAction -> results.add(newRanges)
                        RejectAction -> {}
                        is RedirectAction -> possibleCombinations(newRanges, workflows.getValue(action.workflow))
                    }
                }

                if (doesNotMatch != null) {
                    possibleCombinations(
                        ranges.replaceByName(component, doesNotMatch),
                        remainingRules.subList(1, remainingRules.size)
                    )
                }
            }
        }
    }

    val start = workflows.getValue("in")
    val initialRange = IntRange(1, 4000)

    possibleCombinations(PossibleRanges(initialRange, initialRange, initialRange, initialRange), start)
    val combinations = results.sumOf { it.sizeProduct() }

    println(combinations)
}

private data class PossibleRanges(
    val x: IntRange,
    val m: IntRange,
    val a: IntRange,
    val s: IntRange
) {
    fun byName(c: Char): IntRange {
        return when (c) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException(c.toString())
        }
    }

    fun replaceByName(c: Char, newValue: IntRange): PossibleRanges {
        return when (c) {
            'x' -> this.copy(x = newValue)
            'm' -> this.copy(m = newValue)
            'a' -> this.copy(a = newValue)
            's' -> this.copy(s = newValue)
            else -> throw IllegalArgumentException(c.toString())
        }
    }

    fun sizeProduct(): Long = x.size.toLong() * m.size.toLong() * a.size.toLong() * s.size.toLong()
}

private val IntRange.size
    get() = endInclusive - start + 1

private fun IntRange.splitGreaterThan(conditionValue: Int): RangeSplit {
    return when {
        conditionValue < start -> RangeSplit(this, null)
        conditionValue > endInclusive -> RangeSplit(null, this)
        else -> {
            val doesNotMatch = IntRange(start, conditionValue).takeUnless { it.isEmpty() }
            val matches = IntRange(conditionValue + 1, endInclusive).takeUnless { it.isEmpty() }

            RangeSplit(matches, doesNotMatch)
        }
    }
}

private fun IntRange.splitLessThan(conditionValue: Int): RangeSplit {
    return when {
        conditionValue < start -> RangeSplit(null, this)
        conditionValue > endInclusive -> RangeSplit(this, null)
        else -> {
            val matches = IntRange(start, conditionValue - 1).takeUnless { it.isEmpty() }
            val doesNotMatch = IntRange(conditionValue, endInclusive).takeUnless { it.isEmpty() }

            RangeSplit(matches, doesNotMatch)
        }
    }
}

private data class RangeSplit(
    val matches: IntRange?,
    val doesNotMatch: IntRange?
)