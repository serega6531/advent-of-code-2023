package day5

import java.util.NavigableMap
import java.util.TreeMap

fun solve(maps: List<String>, seeds: Sequence<Long>): Long {
    val (seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = maps
        .map { parseRelationMap(it) }
        .map { it.associateByTo(TreeMap()) { relation -> relation.source } }

    val result = seeds.minOf { seed ->
        val soil = findRelation(seed, seedToSoil)
        val fertilizer = findRelation(soil, soilToFertilizer)
        val water = findRelation(fertilizer, fertilizerToWater)
        val light = findRelation(water, waterToLight)
        val temperature = findRelation(light, lightToTemperature)
        val humidity = findRelation(temperature, temperatureToHumidity)
        val location = findRelation(humidity, humidityToLocation)

        location
    }

    return result
}

private fun parseRelationMap(map: String): List<Relation> {
    return map.lines()
        .map { it.split(' ') }
        .map { it.map { s -> s.toLong() } }
        .map { (destination, source, length) -> Relation(destination, source, length) }
}

private fun findRelation(from: Long, map: NavigableMap<Long, Relation>): Long {
    val relation = map.floorEntry(from)?.value
        ?: return from

    val diff = from - relation.source

    return relation
        .takeIf { diff < it.length }
        ?.let { it.destination + diff }
        ?: from
}

private data class Relation(
    val destination: Long,
    val source: Long,
    val length: Long
)

private operator fun <E> List<E>.component6(): E {
    return get(5)
}

private operator fun <E> List<E>.component7(): E {
    return get(6)
}