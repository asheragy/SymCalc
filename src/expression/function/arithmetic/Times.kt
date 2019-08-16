package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Integer

import java.util.ArrayList

class Times(vararg e: Expr) : FunctionExpr(Function.TIMES, *e) {

    override val properties: Int
        get() = Properties.ASSOCIATIVE.value or Expr.Properties.NumericFunction.value or Properties.Orderless.value

    override fun evaluate(): Expr {

        // Transform 2*(a+b) to 2a + 2b
        if (size == 2 && args[1] is Plus) {
            val newArgs = args[1].args.map { Times(args[0], it) }
            val e = Plus(*newArgs.toTypedArray()).eval()
            if (e !is Plus) // If Plus eval is not any better don't use this value
                return e
        }

        val list = ArrayList<Expr>()
        for (i in 0 until size)
            list += get(i)

        // Transform x^y * x^z to x^yz
        if (list.count { it is Power } > 1) {
            val groups = list.filterIsInstance<Power>().groupBy { it[0] }
            list.removeIf { it is Power }
            for (group in groups) {
                if (group.value.size == 2) {
                    val times = Power(group.key, Plus(group.value[0][1], group.value[1][1])).eval()
                    list.add(times)
                }
                else
                    list.add(group.value[0])
            }
        }

        val numberItems = list.filterIsInstance<NumberExpr>()
        list.removeIf { it is NumberExpr }
        val product = numberItems.fold(Integer.ONE as NumberExpr) { acc, n -> acc * n }
        if (product.isZero)
            return Integer.ZERO
        else if(!product.isOne || list.size == 0)
            list.add(0, product)

        if (list.size > 1) {
            return Times(*list.toTypedArray())
        }

        return list[0]
    }

    override fun toString(): String {
        if (size > 0) {
            val strings = args.map {
                if (it is Plus || it is Subtract)
                    "($it)"
                else
                    it.toString()
            }

            return strings.joinToString(" * ")
        }

        return super.toString()
    }

    override fun validate() {
    }
}
