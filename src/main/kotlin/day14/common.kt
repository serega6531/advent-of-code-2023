package day14

fun String.shiftLeft(): String {
    val sb = StringBuilder(this.length)
    var emptySpace: Int? = null

    this.indices.forEach { index ->
        when (this[index]) {
            'O' -> {
                sb.append('O')

                emptySpace = emptySpace?.plus(1)
            }

            '#' -> {
                if (emptySpace != null) {
                    sb.append(".".repeat(index - emptySpace!!))
                }
                emptySpace = null
                sb.append('#')
            }

            '.' -> {
                if (emptySpace == null) {
                    emptySpace = index
                }
            }
        }
    }

    if (emptySpace != null) {
        sb.append(".".repeat(this.length - emptySpace!!))
    }

    return sb.toString()
}