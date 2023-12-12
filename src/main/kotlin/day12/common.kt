package day12

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
    val withOperational = calculatePermutations(conditions.with(firstUnknown, '.'), groups)
    val withDamaged = calculatePermutations(conditions.with(firstUnknown, '#'), groups)

    return withOperational + withDamaged
}

private fun isCorrectPrefix(conditions: String, groups: List<Int>): Boolean {
    val firstUnknown = conditions.indexOf('?')
    val beforeUnknown = conditions.substring(0, firstUnknown)

    val prefixGroups = beforeUnknown.split(Regex("\\.+"))
        .map { it.count() }
        .filter { it != 0 }

    val margin = if (firstUnknown > 0 && conditions[firstUnknown - 1] == '#') {
        1
    } else {
        0
    }

    val truncated = prefixGroups.subList(0, (prefixGroups.size - margin).coerceAtLeast(0))

    return groups.startsWith(truncated)
}

private fun isCorrectPermutation(conditions: String, groups: List<Int>): Boolean {
    val actualGroups = conditions.split(Regex("\\.+")).map { it.count() }.filter { it != 0 }
    return groups == actualGroups
}

private fun String.with(index: Int, c: Char): String {
    return this.toCharArray().also { it[index] = c }.concatToString()
}

private fun List<Int>.startsWith(prefix: List<Int>): Boolean {
    return prefix.size <= this.size && subList(0, prefix.size) == prefix
}