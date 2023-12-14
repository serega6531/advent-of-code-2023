package day12

private val functionalSpringsRegex = Regex("\\.+")

fun calculatePermutations(conditions: String, groups: List<Int>): Int {
    if (conditions.none { it == '?' }) {
        return if (isCorrectPermutation(conditions, groups)) {
            1
        } else {
            0
        }

    }

    if (!isCorrectPrefix(conditions, groups)) {
        return 0
    }

    val firstUnknown = conditions.indexOfFirst { it == '?' }
    val withDamaged = calculatePermutations(conditions.with(firstUnknown, '#'), groups)
    val withOperational = calculatePermutations(conditions.with(firstUnknown, '.'), groups)

    return withOperational + withDamaged
}

private fun isCorrectPrefix(conditions: String, groups: List<Int>): Boolean {
    val firstUnknown = conditions.indexOf('?')
    val beforeUnknown = conditions.substring(0, firstUnknown)

    val prefixGroups = beforeUnknown.split(functionalSpringsRegex)
        .map { it.count() }
        .filter { it != 0 }

    if (prefixGroups.isEmpty()) {
        return true
    }

    if (prefixGroups.size >= groups.size) { // assuming there are no question marks left
        return false
    }

    val groupsLeft = if (firstUnknown > 0 && conditions[firstUnknown - 1] == '#') {
        groups.takeLast(groups.size - prefixGroups.size - 1)
    } else {
        groups.takeLast(groups.size - prefixGroups.size)
    }

    val minCharsRequired = groupsLeft.sum() + (groupsLeft.size - 1)

    if (conditions.length - firstUnknown < minCharsRequired) {
        return false
    }

    return if (firstUnknown > 0 && conditions[firstUnknown - 1] == '#') {
        val truncated = prefixGroups.subList(0, prefixGroups.size - 1)
        groups.startsWith(truncated) && prefixGroups.last() <= groups[prefixGroups.size - 1]
    } else {
        groups.startsWith(prefixGroups.subList(0, prefixGroups.size))
    }
}

private fun isCorrectPermutation(conditions: String, groups: List<Int>): Boolean {
    val actualGroups = conditions.split(functionalSpringsRegex).map { it.count() }.filter { it != 0 }
    return groups == actualGroups
}

private fun String.with(index: Int, c: Char): String {
    return this.toCharArray().also { it[index] = c }.concatToString()
}

private fun List<Int>.startsWith(prefix: List<Int>): Boolean {
    return prefix.size <= this.size && subList(0, prefix.size) == prefix
}