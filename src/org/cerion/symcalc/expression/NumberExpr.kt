package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum

abstract class NumberExpr : Expr(), Comparable<NumberExpr> {

    override val type: ExprType
        get() = ExprType.NUMBER

    override val isInteger: Boolean
        get() = numType() == INTEGER

    abstract val isZero: Boolean
    abstract val isOne: Boolean

    val isReal: Boolean
        get() = numType() == REAL

    val isRational: Boolean
        get() = numType() == RATIONAL

    val isComplex: Boolean
        get() = numType() == COMPLEX

    abstract fun numType(): Int  //getType already used by MathTerm

    abstract override fun toString(): String
    abstract fun add(num: NumberExpr): NumberExpr
    abstract fun subtract(num: NumberExpr): NumberExpr
    abstract fun multiply(num: NumberExpr): NumberExpr
    abstract fun divide(num: NumberExpr): NumberExpr
    abstract fun power(num: NumberExpr): Expr
    abstract fun canExp(num: NumberExpr): Boolean  //this^num = num is TRUE, FALSE if can't resolve
    abstract fun negate(): NumberExpr

    abstract fun toDouble(): Double  //Valid on all but ComplexNum
    abstract fun equals(e: NumberExpr): Boolean

    override fun equals(e: Expr): Boolean = e.isNumber && equals(e as NumberExpr)
    override fun show(i: Int) = indent(i, "Number " + this.toString())
    public override fun evaluate(): NumberExpr = this

    companion object {
        //Types
        // TODO make this enum
        const val INTEGER = 0
        const val RATIONAL = 1
        const val REAL = 2
        const val COMPLEX = 3

        @JvmStatic fun parse(s: String): NumberExpr {
            if (s.indexOf('i') > -1)
                return ComplexNum(s)

            return if (s.indexOf('.') > 0) RealNum.parse2(s) else IntegerNum(s)

        }
    }
}
