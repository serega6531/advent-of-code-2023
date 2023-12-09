package day9

fun getNext(values: List<Int>): Int {
    if (values.all { it == 0 }) {
        return 0
    }

    val diffs = values.zipWithNext { a, b -> b - a }
    return values.last() + getNext(diffs)
}