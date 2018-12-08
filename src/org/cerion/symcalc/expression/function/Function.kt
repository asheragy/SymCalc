package org.cerion.symcalc.expression.function

enum class Function constructor(val value: String) {
    N("N"),
    HOLD("Hold"),
    NUMBERQ("NumberQ"),
    NUMERICQ("NumericQ"),

    // Core
    SET("Set"),
    COMPOUND_EXPRESSION("CompoundExpression"),

    PLUS("Plus"),
    SUBTRACT("Subtract"),
    TIMES("Times"),
    DIVIDE("Divide"),
    POWER("Power"),
    SQRT("Sqrt"),

    // Trig
    SIN("Sin"),
    COS("Cos"),
    TAN("Tan"),

    //List Functions
    TOTAL("Total"),
    RANGE("Range"),
    REVERSE("Reverse"),
    FIRST("First"),
    LAST("Last"),
    TABLE("Table"),
    FLATTEN("Flatten"),
    PARTITION("Partition"),
    JOIN("Join"),
    SELECT("Select"),
    VECTORQ("VectorQ"),
    MATRIXQ("MatrixQ"),
    DOT("Dot"),
    IDENTITY_MATRIX("IdentityMatrix"),
    TALLY("Tally"),

    //IntegerNum
    FACTORIAL("Factorial"),
    MOD("Mod"),
    POWERMOD("PowerMod"),
    GCD("GCD"),
    FOURIER("Fourier"),
    PRIMEQ("primeQ"),
    FIBONACCI("Fibonacci"),
    FACTOR("Factor"),
    BINOMIAL("Binomial"),
    INTEGER_DIGITS("IntegerDigits"),
    RANDOM_INTEGER("RandomInteger"),
    BERNOULLI("Bernoulli"),
    EVENQ("EvenQ"),
    ODDQ("OddQ"),

    // Calculus
    D("D"),

    //Logical
    GREATER("Greater"),

    //Statistics
    MEAN("Mean"),
    VARIANCE("Variance"),
    STANDARD_DEVIATION("StandardDeviation"),
    RANDOM_CHOICE("RandomChoice"),

    // Graphics
    PLOT("Plot"),

    // Procedural
    IF("If"),


    ASDFDSF("DSFDS");

    override fun toString(): String {
        return this.value
    }

}