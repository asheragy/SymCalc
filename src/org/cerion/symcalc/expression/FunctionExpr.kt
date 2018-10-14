package org.cerion.symcalc.expression

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.core.NumberQ
import org.cerion.symcalc.expression.function.core.NumericQ
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.plots.Plot
import org.cerion.symcalc.expression.function.integer.*
import org.cerion.symcalc.expression.function.list.*
import org.cerion.symcalc.expression.function.logical.Greater
import org.cerion.symcalc.expression.function.statistics.*

abstract class FunctionExpr protected constructor(val functionType: FunctionType, vararg val e: Expr) : Expr() {
    val name: String

    val isNumeric: Boolean
        get() = hasProperty(Expr.Properties.NumericFunction)

    init {
        name = functionType.toString()

        setArgs(*e)
    }

    override val type: ExprType
        get() = Expr.ExprType.FUNCTION

    override fun toString(): String {
        return name + argString()
    }

    override fun show(i: Int) {
        indent(i, "$name[]")
        for (j in 0 until size())
            get(j).show(i + 1)
    }

    // TODO make this required for subclasses
    @Throws(ValidationException::class)
    open fun validate() {
    }

    override fun equals(e: Expr): Boolean {
        if (!e.isFunction)
            return false

        val f = e as FunctionExpr
        if (f.functionType != functionType)
            return false

        if (f.size() != size())
            return false

        for (i in 0 until size()) {
            if (!get(i).equals(f[i]))
                return false
        }

        return true
    }

    fun add(t: Expr) {
        setArgs(t)
    }

    enum class FunctionType constructor(val value: String) {
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

    @Throws(ValidationException::class)
    protected fun validateParameterType(position: Int, type: Expr.ExprType) {
        if (size() < position || get(position).type != type)
            throw ValidationException(String.format("parameter %d must be type %s", position, type))
    }

    @Throws(ValidationException::class)
    protected fun validateNumberType(position: Int, numberType: Int) {
        val e = get(position) as NumberExpr
        if (e.numType() != numberType)
            throw ValidationException(String.format("parameter %d must be type %s", position, numberType))
    }

    @Throws(ValidationException::class)
    protected fun validateParameterCount(expected: Int) {
        if (size() == expected)
            return

        val error = String.format("%d parameters, expected %d", size(), expected)
        throw ValidationException(error)
    }

    companion object {

        private fun stringToFunctionType(functionName: String): FunctionType? {
            val name = functionName.toLowerCase()
            for (t in FunctionType.values()) {
                if (t.value.toLowerCase().contentEquals(name))
                    return t
            }

            return null
        }

        @JvmStatic
        fun isValidFunction(functionName: String): Boolean {
            val name = functionName.toLowerCase()

            return stringToFunctionType(name) != null
        }

        @JvmStatic
        fun createFunction(f: String, vararg e: Expr): FunctionExpr {
            val type = stringToFunctionType(f)

            when (type) {
                FunctionExpr.FunctionType.N -> return N(*e)
                FunctionExpr.FunctionType.HOLD -> return Hold(*e)
                FunctionExpr.FunctionType.NUMBERQ -> return NumberQ(*e)
                FunctionExpr.FunctionType.NUMERICQ -> return NumericQ(*e)

                FunctionExpr.FunctionType.PLUS -> return Plus(*e)
                FunctionExpr.FunctionType.SUBTRACT -> return Subtract(*e)
                FunctionExpr.FunctionType.TIMES -> return Times(*e)
                FunctionExpr.FunctionType.DIVIDE -> return Divide(*e)
                FunctionExpr.FunctionType.POWER -> return Power(*e)
                FunctionExpr.FunctionType.SIN -> return Sin(*e)

                //List
                FunctionExpr.FunctionType.TOTAL -> return Total(*e)
                FunctionExpr.FunctionType.RANGE -> return Range(*e)
                FunctionExpr.FunctionType.REVERSE -> return Reverse(*e)
                FunctionExpr.FunctionType.FIRST -> return First(*e)
                FunctionExpr.FunctionType.LAST -> return Last(*e)
                FunctionExpr.FunctionType.TABLE -> return Table(*e)
                FunctionExpr.FunctionType.FLATTEN -> return Flatten(*e)
                FunctionExpr.FunctionType.PARTITION -> return Partition(*e)
                FunctionExpr.FunctionType.JOIN -> return Join(*e)
                FunctionExpr.FunctionType.SELECT -> return Select(*e)
                FunctionExpr.FunctionType.VECTORQ -> return VectorQ(*e)
                FunctionExpr.FunctionType.MATRIXQ -> return MatrixQ(*e)
                FunctionExpr.FunctionType.DOT -> return Dot(*e)

                //IntegerNum
                FunctionExpr.FunctionType.FACTORIAL -> return Factorial(*e)
                FunctionExpr.FunctionType.MOD -> return Mod(*e)
                FunctionExpr.FunctionType.POWERMOD -> return PowerMod(*e)
                FunctionExpr.FunctionType.GCD -> return GCD(*e)
                FunctionExpr.FunctionType.FOURIER -> return Fourier(*e)
                FunctionExpr.FunctionType.PRIMEQ -> return PrimeQ(*e)
                FunctionExpr.FunctionType.FIBONACCI -> return Fibonacci(*e)
                FunctionExpr.FunctionType.FACTOR -> return Factor(*e)
                FunctionExpr.FunctionType.BINOMIAL -> return Binomial(*e)
                FunctionExpr.FunctionType.BERNOULLI -> return Bernoulli(*e)
                FunctionExpr.FunctionType.INTEGER_DIGITS -> return IntegerDigits(*e)
                FunctionExpr.FunctionType.EVENQ -> return EvenQ(*e)
                FunctionExpr.FunctionType.ODDQ -> return OddQ(*e)

                //Logical
                FunctionExpr.FunctionType.GREATER -> return Greater(*e)

                //Statistics
                FunctionExpr.FunctionType.MEAN -> return Mean(*e)
                FunctionExpr.FunctionType.VARIANCE -> return Variance(*e)
                FunctionExpr.FunctionType.STANDARD_DEVIATION -> return StandardDeviation(*e)
                FunctionExpr.FunctionType.RANDOM_CHOICE -> return RandomChoice(*e)
                FunctionExpr.FunctionType.RANDOM_INTEGER -> return RandomInteger(*e)

                // Graphics
                FunctionExpr.FunctionType.PLOT -> return Plot(*e)

                else -> throw RuntimeException("invalid function name '$f'")
            }
        }
    }
}