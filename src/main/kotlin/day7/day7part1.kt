package day7

import getResourceAsText

fun main() {
    val input = getResourceAsText("/day7/input.txt")

    val result = input.lines()
        .map { it.split(' ') }
        .map { (hand, bid) -> HandAndBid(hand, bid.toInt()) }
        .sortedWith(HandComparator)
        .mapIndexed { index, (_, bid) -> (index + 1) * bid }
        .sum()

    println(result)
}

object HandComparator : Comparator<HandAndBid> {

    private val labels = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')

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
        val freq = hand.groupingBy { it }.eachCount().values.sortedDescending()

        return when (freq) {
            listOf(5) -> HandType.FIVE_OF_A_KIND
            listOf(4, 1) -> HandType.FOUR_OF_A_KIND
            listOf(3, 2) -> HandType.FULL_HOUSE
            listOf(3, 1, 1) -> HandType.THREE_OF_A_KIND
            listOf(2, 2, 1) -> HandType.TWO_PAIR
            listOf(2, 1, 1, 1) -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    enum class HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

}

data class HandAndBid(
    val hand: String,
    val bid: Int
)