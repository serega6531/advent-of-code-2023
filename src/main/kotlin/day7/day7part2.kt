package day7

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day7/input.txt")

    val result = input.lines()
        .map { it.split(' ') }
        .map { (hand, bid) -> HandAndBid(hand, bid.toInt()) }
        .sortedWith(JokerHandComparator)
        .mapIndexed { index, (_, bid) -> (index + 1) * bid }
        .sum()

    println(result)
}

private object JokerHandComparator : Comparator<HandAndBid> {

    private val labels = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    override fun compare(o1: HandAndBid, o2: HandAndBid): Int {
        val hand1 = o1.hand
        val hand2 = o2.hand

        val type1 = getType(hand1)
        val type2 = getType(hand2)

        if (type1 != type2) {
            return type2.compareTo(type1)
        }

        return compareLabels(hand1, hand2)
    }

    private fun compareLabels(hand1: String, hand2: String): Int {
        return hand1.zip(hand2)
            .map { (l1, l2) -> labels.indexOf(l1) to labels.indexOf(l2) }
            .first { (i1, i2) -> i1 != i2 }
            .let { (i1, i2) -> i2.compareTo(i1) }
    }

    private fun getType(hand: String): HandType {
        val frequencies = hand.groupingBy { it }.eachCount()
        val jokers = frequencies['J'] ?: 0

        if (jokers == 0) {
            return getTypeSimple(frequencies)
        }

        val withoutJokers = frequencies.minus('J')
        val maxLabel = withoutJokers.maxByOrNull { it.value }

        if (maxLabel == null) { // only jokers
            return HandType.FIVE_OF_A_KIND
        }

        val updated = maxLabel.key to (maxLabel.value + jokers)
        return getTypeSimple(withoutJokers.plus(updated))
    }

    private fun getTypeSimple(frequencies: Map<Char, Int>): HandType {
        val counts = frequencies.values.sortedDescending()

        return when (counts) {
            listOf(5) -> HandType.FIVE_OF_A_KIND
            listOf(4, 1) -> HandType.FOUR_OF_A_KIND
            listOf(3, 2) -> HandType.FULL_HOUSE
            listOf(3, 1, 1) -> HandType.THREE_OF_A_KIND
            listOf(2, 2, 1) -> HandType.TWO_PAIR
            listOf(2, 1, 1, 1) -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

}