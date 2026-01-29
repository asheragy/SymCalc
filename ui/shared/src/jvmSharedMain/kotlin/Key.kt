

enum class Key {
    DEL,
    EVAL,
    CLEAR,
    NUM_0,
    NUM_1,
    NUM_2,
    NUM_3,
    NUM_4,
    NUM_5,
    NUM_6,
    NUM_7,
    NUM_8,
    NUM_9,
    DOT,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,

    SIN,
    COS,
    TAN,
    LN,
    LOG,
    SQRT,
    SQUARE,
    E,
    PI,
    POW,
    FACTORIAL,
    BRACKET_LEFT,
    BRACKET_RIGHT,
    DEBUG,
    NOOP;

    fun inputValue(): String {
        if (ordinal >= NUM_0.ordinal && ordinal <= NUM_9.ordinal)
            return (ordinal - NUM_0.ordinal).toString()

        return when(this) {
            DOT -> "."
            PLUS -> "+"
            DIVIDE -> "/"
            TIMES -> "*"
            MINUS -> "-"
            BRACKET_LEFT -> "("
            BRACKET_RIGHT -> ")"
            FACTORIAL -> "!"
            POW -> "^"
            E -> "E"
            PI -> "Pi"
            SIN -> "Sin("
            COS -> "Cos("
            TAN -> "Tan("
            SQRT -> "Sqrt("
            SQUARE -> "^2"
            LOG -> "Log10("
            LN -> "Log("
            else -> throw UnsupportedOperationException()
        }
    }
}

