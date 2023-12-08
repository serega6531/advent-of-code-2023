package day8

import getResourceAsText
import java.math.BigInteger

fun main() {
    val (instructions, nodesString) = getResourceAsText("/day8/input.txt").split("\n\n")

    val nodeRegex = Regex("""([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)""")

    val nodes = nodesString.lines()
        .map { nodeRegex.matchEntire(it)!! }
        .map { it.destructured }
        .associate { (source, left, right) -> source to (left to right) }

    val periods = nodes.keys.filter { it.endsWith('A') }
        .map { getNodeSequence(it, instructions, nodes) }
        .map { seq -> seq.first { it.first.endsWith('Z') } }
        .map { it.second }

    val result = periods.lcm() // didn't come up with the solution myself :(

    println(result)
}

private fun List<Long>.lcm(): BigInteger {
    return this.map { it.toBigInteger() }
        .reduce { acc, num -> findLCM(acc, num) }
}

private fun findLCM(a: BigInteger, b: BigInteger): BigInteger {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == BigInteger.ZERO && lcm % b == BigInteger.ZERO) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}
