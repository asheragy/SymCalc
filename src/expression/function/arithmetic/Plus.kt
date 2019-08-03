package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

import java.util.ArrayList
import kotlin.math.min

class Plus(vararg e: Expr) : FunctionExpr(Function.PLUS, *e) {

    override val properties: Int
        get() = Expr.Properties.ASSOCIATIVE.value or Expr.Properties.NumericFunction.value or Properties.Orderless.value

    override fun evaluate(): Expr {

        var sum: NumberExpr = IntegerNum.ZERO
        val list = ArrayList<Expr>()

        for (i in 0 until size)
            list.add(get(i))

        // TODO this should be done in Expr class, may only apply to ConstExpr so that is a factor
        val minPrecision = list.minBy { it.precision }!!.precision
        for (i in 0 until list.size) {
            if (list[i].precision > minPrecision) {
                list[i] = list[i].eval(minPrecision)
            }
        }

        val it = list.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e.isNumber) {
                sum+= e.asNumber()
                it.remove()
            }
        }

        //At least 1 non-number value
        if (list.size > 0) {
            //Only add number if non-zero
            if (!sum.isZero)
                list.add(0, sum)

            //If only 1 entry just return it
            return if (list.size == 1) list[0] else Plus(*list.toTypedArray())

        }

        return sum
    }

    operator fun plusAssign(e: Expr) = add(e)

    override fun toString(): String {
        if (size > 0) {
            return args.joinToString(" + ")
        }

        return super.toString()
    }

    override fun validate() {
    }
}
