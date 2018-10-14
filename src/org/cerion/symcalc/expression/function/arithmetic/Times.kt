package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

import java.util.ArrayList

class Times(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.TIMES, *e) {

    override val properties: Int
        get() = Expr.Properties.ASSOCIATIVE.value

    override fun evaluate(): Expr {

        var product: NumberExpr = IntegerNum.ONE
        val list = ArrayList<Expr>()

        for (i in 0 until size())
            list += get(i)

        //Multiply numbers
        val it = list.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e.isNumber) {
                val n = e as NumberExpr
                if (!n.isOne)
                    product = product.multiply(n)
                it.remove()
            }
        }

        //At least 1 non-number value
        if (list.size > 0) {
            //Only add number if not one
            if (!product.isOne)
                list.add(0, product)

            return if (list.size == 1) list[0] else Times(*list.toTypedArray())

        }

        return product
    }

    override fun toString(): String {
        if (size() > 0) {
            var s = get(0).toString()
            for (i in 1 until size())
                s += " * " + get(i).toString()

            return s
        }

        return super.toString()
    }
}
