fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText()!!

fun Iterable<Int>.product(): Int {
    return this.drop(1).reduce { a, b -> a * b }
}

fun List<String>.transpose(): List<String> =
    buildList {
        this@transpose.first().indices.forEach { index ->
            val sb = StringBuilder()

            this@transpose.forEach { line ->
                sb.append(line[index])
            }

            add(sb.toString())
        }
    }

typealias YX = Pair<Int, Int>