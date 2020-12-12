package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr

class Times(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.Flat.value or Properties.NumericFunction.value or Properties.Orderless.value

    override fun evaluate(): Expr {

        // Transform 2*(a+b) to 2a + 2b
        if (size == 2 && args[1] is Plus) {
            val newArgs = (args[1] as Plus).args.map { Times(args[0], it) }
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

        // Factor non-numeric duplicates
        val nonNumerics = list.filter { it !is NumberExpr }
        val groups = nonNumerics.groupBy { it }
        for (group in groups) {
            if(group.value.size > 1) {
                list.removeAll { it == group.value[0] }
                list.add(Integer(group.value.size))
                list.add(group.value[0])
            }
        }

        val numberItems = list.filterIsInstance<NumberExpr>()
        if (numberItems.contains(Integer.ZERO)) // If zero is an integer return integer/infinite precision rather than double/etc
            return Integer.ZERO

        list.removeIf { it is NumberExpr }
        val product = numberItems.fold(Integer.ONE as NumberExpr) { acc, n -> acc * n }
        if(!product.isOne || list.size == 0)
            list.add(0, product)

        if (list.size == 1)
            return list[0]

        if (list.contains(ComplexInfinity()))
            return ComplexInfinity()

        return Times(*list.toTypedArray())
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
