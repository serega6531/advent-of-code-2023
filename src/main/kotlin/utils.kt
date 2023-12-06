fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText()!!

fun Iterable<Int>.product(): Int {
    return this.drop(1).fold(this.first()) { acc, i -> acc * i }
}