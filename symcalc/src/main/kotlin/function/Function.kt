package org.cerion.symcalc.function

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
    LOG("Log"),
    LOG10("Log10"),
    EXP("Exp"),
    MINUS("Minus"),

    // Trig
    SIN("Sin"),
    COS("Cos"),
    TAN("Tan"),
    SEC("Sec"),
    CSC("Csc"),
    COT("Cot"),
    ARCSIN("ArcSin"),
    ARCCOS("ArcCos"),
    ARCTAN("ArcTan"),
    ARCSEC("ArcSec"),
    ARCCSC("ArcCsc"),
    ARCCOT("ArcCot"),
    // Sinc Haversine InverseHaversine

    // Hyperbolic
    SINH("Sinh"),
    COSH("Cosh"),
    TANH("Tanh"),
    SECH("Sech"),
    CSCH("Csch"),
    COTH("Coth"),
    ARCSINH("ArcSinh"),
    ARCCOSH("ArcCosh"),
    ARCTANH("ArcTanh"),
    ARCSECH("ArcSech"),
    ARCCSCH("ArcCsch"),
    ARCCOTH("ArcCoth"),
    // Gudermannian InverseGudermannian

    // List Functions
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
    MAP("Map"),
    CONSTANT_ARRAY("ConstantArray"),

    // Integer
    FACTORIAL("Factorial"),
    FACTORIAL2("Factorial2"),
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
    EULERPHI("EulerPhi"),
    EULERE("EulerE"),
    DIVISORS("Divisors"),
    DIVISORSIGMA("DivisorSigma"),

    // Numeric
    FLOOR("Floor"),

    // Calculus
    D("D"),
    SUM("Sum"),

    //Logical
    EQUAL("Equal"),
    GREATER("Greater"),

    //Statistics
    MEAN("Mean"),
    VARIANCE("Variance"),
    STANDARD_DEVIATION("StandardDeviation"),
    RANDOM_CHOICE("RandomChoice"),

    // Procedural
    IF("If"),

    // Special
    GAMMA("Gamma"),
    POLYGAMMA("PolyGamma"),
    ZETA("Zeta"),
    POCHHAMMER("Pochhammer"),

    END_OF_LIST("**eol");

    override fun toString(): String {
        return this.value
    }

}