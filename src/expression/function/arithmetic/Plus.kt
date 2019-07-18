package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

import java.util.ArrayList

class Plus(vararg e: Expr) : FunctionExpr(Function.PLUS, *e) {

    override val properties: Int
        get() = Expr.Properties.ASSOCIATIVE.value or Expr.Properties.NumericFunction.value

    override fun evaluate(): Expr {

        var sum: NumberExpr = IntegerNum.ZERO
        val list = ArrayList<Expr>()

        //list.addAll(getArgs());
        for (i in 0 until size())
            list.add(get(i))

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
        if (size() > 0) {
            var s = get(0).toString()
            for (i in 1 until size())
                s += " + " + get(i).toString()

            return s
        }

        return super.toString()
    }
}
