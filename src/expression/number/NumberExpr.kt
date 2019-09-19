package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ExprBase
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

interface INumberExpr {
    val isZero: Boolean
    val isOne: Boolean
    val numType: NumberType
    val isNegative: Boolean

    fun evaluate(precision: Int): NumberExpr

    val precision: Int
    override fun toString(): String

    fun compareTo(other: NumberExpr): Int
    fun evaluate(): NumberExpr = this as NumberExpr

}

@Suppress("CovariantEquals")
abstract class NumberExpr : ExprBase(), Comparable<NumberExpr>, INumberExpr {

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

    override fun equals(e: Expr): Boolean {
        if (this::class != e::class)
            return false

        return try {
            compareTo(e as NumberExpr) == 0
        }
        // Any comparison to complex number with non-zero imaginary is invalid, except equals (always false)
        catch (e: UnsupportedOperationException) {
            false
        }
    }

    companion object {
        @JvmStatic fun parse(s: String): NumberExpr {
            if (s.indexOf('i') > -1) {
                val num = s.substring(0, s.length - 1)

                if (num.isEmpty())
                //If passed just "i" its complex number 1
                    return Complex(Integer.ZERO, Integer.ONE)
                else
                    return Complex(Integer.ZERO, parse(num))
            }

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
