package org.cerion.symcalc.number

import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.trig.ArcTan
import java.math.BigDecimal

enum class NumberType {
    INTEGER,
    RATIONAL,
    REAL_DOUBLE,
    REAL_BIGDEC,
    COMPLEX
}

@Suppress("CovariantEquals")
sealed class NumberExpr : Expr(), Comparable<NumberExpr> {
    abstract val isZero: Boolean
    abstract val isOne: Boolean
    abstract val numType: NumberType
    abstract val isNegative: Boolean

    abstract fun toPrecision(precision: Int): NumberExpr
    abstract override val precision: Int
    abstract override fun toString(): String

    abstract operator fun plus(other: NumberExpr): NumberExpr
    open operator fun minus(other: NumberExpr): NumberExpr = this + other.unaryMinus()
    abstract operator fun times(other: NumberExpr): NumberExpr
    abstract operator fun div(other: NumberExpr): NumberExpr
    abstract operator fun unaryMinus(): NumberExpr

    open fun square(): NumberExpr = this * this
    abstract infix fun pow(other: NumberExpr): NumberExpr

    open fun abs(): Expr = if (isNegative) this.unaryMinus() else this
    abstract fun floor(): NumberExpr
    abstract fun round(): NumberExpr
    abstract fun quotient(other: NumberExpr): NumberExpr
    operator fun rem(other: NumberExpr): NumberExpr = this - (other * this.quotient(other))

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
                return if (num.isEmpty())
                    Complex.I //If passed just "i" its complex number 1
                else
                    Complex(Integer.ZERO, parse(num))
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

    fun arg(): Expr {
        return if (this is Complex)
            ArcTan(img / real)
        else if (this.isNegative)
            Pi()
        else
            Integer.ZERO
    }
}
