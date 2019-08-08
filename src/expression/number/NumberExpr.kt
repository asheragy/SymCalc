package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr
import java.lang.Exception
import java.lang.UnsupportedOperationException

enum class NumberType {
    INTEGER,
    RATIONAL,
    REAL,
    COMPLEX
}

@Suppress("CovariantEquals")
abstract class NumberExpr : Expr(), Comparable<NumberExpr> {

    override val type: ExprType get() = ExprType.NUMBER
    override val isInteger: Boolean get() = numType == NumberType.INTEGER

    abstract override val precision: Int
    abstract val isZero: Boolean
    abstract val isOne: Boolean
    abstract val numType: NumberType
    abstract val isNegative: Boolean

    val isReal: Boolean get() = numType == NumberType.REAL
    val isRational: Boolean get() = numType == NumberType.RATIONAL
    val isComplex: Boolean get() = numType == NumberType.COMPLEX

    fun asRational(): Rational = this as Rational
    fun asComplex(): ComplexNum = this as ComplexNum

    abstract override fun toString(): String
    abstract fun toDouble(): Double  //Valid on all but ComplexNum
    abstract fun evaluate(precision: Int): NumberExpr

    abstract override fun compareTo(other: NumberExpr): Int

    fun equals(other: NumberExpr): Boolean {
        return try {
            compareTo(other) == 0
        }
        // Any comparison to complex number with non-zero imaginary is invalid, except equals (always false)
        catch (e: UnsupportedOperationException) {
            false
        }
    }

    override fun equals(e: Expr): Boolean = e.isNumber && equals(e as NumberExpr)

    override fun treeForm(i: Int) = indent(i, "Number $this")
    public override fun evaluate(): NumberExpr = this

    abstract operator fun plus(other: NumberExpr): NumberExpr
    abstract operator fun times(other: NumberExpr): NumberExpr
    abstract operator fun div(other: NumberExpr): NumberExpr
    abstract operator fun unaryMinus(): NumberExpr

    operator fun minus(other: NumberExpr): NumberExpr {
        return this + other.unaryMinus()
    }

    companion object {
        @JvmStatic fun parse(s: String): NumberExpr {
            if (s.indexOf('i') > -1)
                return ComplexNum(s)

            return if (s.indexOf('.') > 0) RealNum.create(s) else IntegerNum(s)

        }

        @JvmStatic fun create(n: Number) : NumberExpr {
            if (n is Double)
                return RealNum.create(n)
            else if (n is Int)
                return IntegerNum(n)

            throw Exception()
        }
    }
}
