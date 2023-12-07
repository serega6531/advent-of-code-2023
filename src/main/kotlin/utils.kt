fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText()!!

fun Iterable<Int>.product(): Int {
    return this.drop(1).reduce { a, b -> a * b }
}