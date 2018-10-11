fun main(args: Array<String>) {

    isValid("{]")
}


fun isValid(s: String): Boolean {
    val opening = "({["
    val closing = ")}]"

    if(s.isEmpty())
        return true

    if(s.length < 2)
        return false

    val first = s[0]
    val next = s[1]
    val last = s.last()

    if(opening.contains(first)) {
        val expected = closing[opening.indexOf(first)]

        if(next == expected)
            return isValid(s.substring(2))
        if(next == last)
            return isValid(s.substring(1, s.length - 1))
    }

    return false
}