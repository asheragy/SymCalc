package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr
import java.lang.Exception

enum class NumberType {
    INTEGER,
    RATIONAL,
    REAL,
    COMPLEX
}

abstract class NumberExpr : Expr(), Comparable<NumberExpr> {

    override val type: ExprType get() = ExprType.NUMBER
    override val isInteger: Boolean get() = numType == NumberType.INTEGER

    abstract val isZero: Boolean
    abstract val isOne: Boolean
    abstract val numType: NumberType
    abstract val isNegative: Boolean

    val isReal: Boolean get() = numType == NumberType.REAL
    val isRational: Boolean get() = numType == NumberType.RATIONAL
    val isComplex: Boolean get() = numType == NumberType.COMPLEX

    fun asRational(): RationalNum = this as RationalNum
    fun asComplex(): ComplexNum = this as ComplexNum

    abstract override fun toString(): String
    abstract fun power(other: NumberExpr): Expr // TODO should return numberExpr
    abstract fun canExp(other: NumberExpr): Boolean  //this^num = num is TRUE, FALSE if can't resolve
    abstract fun toDouble(): Double  //Valid on all but ComplexNum

    abstract override fun compareTo(other: NumberExpr): Int

    fun equals(other: NumberExpr): Boolean {
        // TODO ignore type so 1.0==1?

        try {
            return compareTo(other) == 0
        }
        catch (e: NotImplementedError) {
            return false
        }

        /*
        if(numType == other.numType) {
            if (isComplex && other.isComplex)
                return asComplex().real.compareTo(other.asComplex().real) == 0 && asComplex().img.compareTo(other.asComplex().img) == 0
            return compareTo(other) == 0
        }

        return false
        */
    }

    override fun equals(e: Expr): Boolean = e.isNumber && equals(e as NumberExpr)

    override fun treeForm(i: Int) = indent(i, "Number " + this.toString())
    public override fun evaluate(): NumberExpr = this

    abstract operator fun plus(other: NumberExpr): NumberExpr
    abstract operator fun minus(other: NumberExpr): NumberExpr
    abstract operator fun times(other: NumberExpr): NumberExpr
    abstract operator fun div(other: NumberExpr): NumberExpr
    abstract operator fun unaryMinus(): NumberExpr

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
