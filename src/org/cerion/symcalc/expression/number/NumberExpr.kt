package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr

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

    val isReal: Boolean get() = numType == NumberType.REAL
    val isRational: Boolean get() = numType == NumberType.RATIONAL
    val isComplex: Boolean get() = numType == NumberType.COMPLEX

    fun asRational(): RationalNum = this as RationalNum
    fun asComplex(): ComplexNum = this as ComplexNum

    abstract override fun toString(): String
    abstract fun power(num: NumberExpr): Expr // TODO should return numberExpr
    abstract fun canExp(num: NumberExpr): Boolean  //this^num = num is TRUE, FALSE if can't resolve
    abstract fun toDouble(): Double  //Valid on all but ComplexNum
    abstract fun equals(e: NumberExpr): Boolean

    override fun equals(e: Expr): Boolean = e.isNumber && equals(e as NumberExpr)
    override fun show(i: Int) = indent(i, "Number " + this.toString())
    public override fun evaluate(): NumberExpr = this

    abstract operator fun plus(number: NumberExpr): NumberExpr
    abstract operator fun minus(number: NumberExpr): NumberExpr
    abstract operator fun times(num: NumberExpr): NumberExpr
    abstract operator fun div(num: NumberExpr): NumberExpr
    abstract operator fun unaryMinus(): NumberExpr

    companion object {
        //Types
        // TODO make this enum
        const val INTEGER = 0
        //const val REAL = 2

        @JvmStatic fun parse(s: String): NumberExpr {
            if (s.indexOf('i') > -1)
                return ComplexNum(s)

            return if (s.indexOf('.') > 0) RealNum.parse2(s) else IntegerNum(s)

        }
    }
}
