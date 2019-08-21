package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Integer

import java.util.ArrayList

class Times(vararg e: Expr) : FunctionExpr(Function.TIMES, *e) {

    override val properties: Int
        get() = Properties.Flat.value or Expr.Properties.NumericFunction.value or Properties.Orderless.value

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

        // Transform same base: x^a * x^b to x^(a+b)
        if (list.count { it is Power } > 1) {
            val groups = list.filterIsInstance<Power>().groupBy { it[0] }
            list.removeIf { it is Power }
            for (group in groups) {
                if (group.value.size > 1) {
                    val times = Power(group.key, Plus(*group.value.map { it[1] }.toTypedArray()))
                    list.add(times.eval())
                }
                else
                    list.add(group.value[0])
            }
        }

        // Transform same exponent: a^x * b^x = (ab)^x
        if (list.count { it is Power } > 1) {
            val groups = list.filterIsInstance<Power>().groupBy { it[1] }
            list.removeIf { it is Power }
            for (group in groups) {
                if (group.value.size > 1) {
                    val t = Times(*group.value.map { it[0] }.toTypedArray())
                    val power = Power(t, group.key)
                    list.add(power.eval())
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
