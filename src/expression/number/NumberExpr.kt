package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.math.BigDecimal

enum class NumberType {
    INTEGER,
    RATIONAL,
    REAL_DOUBLE,
    REAL_BIGDEC,
    COMPLEX
}

@Suppress("CovariantEquals")
abstract class NumberExpr : Expr(), Comparable<NumberExpr> {

    override val type: ExprType get() = ExprType.NUMBER

    abstract override val precision: Int
    abstract val isZero: Boolean
    abstract val isOne: Boolean
    abstract val numType: NumberType
    abstract val isNegative: Boolean

    //val isReal: Boolean get() = numType == NumberType.REAL
    val isRational: Boolean get() = numType == NumberType.RATIONAL
    val isComplex: Boolean get() = numType == NumberType.COMPLEX

    fun asDouble(): RealDouble = this as RealDouble
    fun asBigDec(): RealBigDec = this as RealBigDec
    fun asRational(): Rational = this as Rational
    fun asComplex(): Complex = this as Complex

    abstract override fun toString(): String

    abstract fun evaluate(precision: Int): NumberExpr

    abstract override fun compareTo(other: NumberExpr): Int

    fun equals(other: NumberExpr): Boolean {
        if (this::class != other::class)
            return false

        return try {
            compareTo(other) == 0
        }
        // Any comparison to complex number with non-zero imaginary is invalid, except equals (always false)
        catch (e: UnsupportedOperationException) {
            false
        }
    }

    override fun equals(e: Expr): Boolean = e is NumberExpr && this.equals(e)

    override fun treeForm(i: Int) = indent(i, "Number $this")
    public override fun evaluate(): NumberExpr = this

    abstract operator fun plus(other: NumberExpr): NumberExpr
    abstract operator fun times(other: NumberExpr): NumberExpr
    abstract operator fun div(other: NumberExpr): NumberExpr
    abstract operator fun unaryMinus(): NumberExpr

    operator fun minus(other: NumberExpr): NumberExpr {
        return this + other.unaryMinus()
    }

    fun square(): NumberExpr {
        return this * this
    }

    companion object {
        @JvmStatic fun parse(s: String): NumberExpr {
            if (s.indexOf('i') > -1)
                return Complex(s)

            return if (s.indexOf('.') > 0) {
                val value = java.lang.Double.parseDouble(s)

                if (value.toString().length < s.length)
                    return RealBigDec(BigDecimal(s))

                return RealDouble(value)
            }
            else
                Integer(s)
        }

        @JvmStatic fun create(n: Number) : NumberExpr {
            if (n is Double)
                return RealDouble(n)
            else if (n is Int)
                return Integer(n)

            throw Exception()
        }
    }
}
