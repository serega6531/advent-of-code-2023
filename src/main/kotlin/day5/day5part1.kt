package day5

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day5/input.txt")

    val regex =
        Regex("seeds: ([\\d \\n]+)\\n\\nseed-to-soil map:\\n([\\d \\n]+)\\n\\nsoil-to-fertilizer map:\\n([\\d \\n]+)\\n\\nfertilizer-to-water map:\\n([\\d \\n]+)\\n\\nwater-to-light map:\\n([\\d \\n]+)\\n\\nlight-to-temperature map:\\n([\\d \\n]+)\\n\\ntemperature-to-humidity map:\\n([\\d \\n]+)\\n\\nhumidity-to-location map:\\n([\\d \\n]+)")
    val match = regex.matchEntire(input)!!

    val seeds = match.groupValues[1].split(" ").map { it.toLong() }
    val result = solve(match.groupValues.drop(2), seeds)

    println(result)
}
