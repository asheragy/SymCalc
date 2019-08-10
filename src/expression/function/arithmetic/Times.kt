package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

import java.util.ArrayList

class Times(vararg e: Expr) : FunctionExpr(Function.TIMES, *e) {

    override val properties: Int
        get() = Properties.ASSOCIATIVE.value or Expr.Properties.NumericFunction.value or Properties.Orderless.value

    override fun evaluate(): Expr {

        // Transform 2*(a+b) to 2a + 2b
        if (size == 2 && args[1] is Plus) {
            val p = args[1]
            // TODO if Plus has 3+ args this needs to be changed, should be a way to map rather than using a loop
            val e = Plus(Times(args[0], p[0]), Times(args[0], p[1])).eval()
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

        // TODO may be able to get number vs non number list via filter
        //Multiply numbers
        var product: NumberExpr = IntegerNum.ONE
        val it = list.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e.isNumber) {
                val n = e as NumberExpr
                if (!n.isOne)
                    product*= n
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
        if (size > 0) {
            var s = get(0).toString()
            for (i in 1 until size) {
                val e = get(i)
                if (e is Plus || e is Subtract)
                    s += " * ($e)"
                else
                    s += " * $e"
            }

            return s
        }

        return super.toString()
    }

    override fun validate() {
    }
}
