package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

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
        // TODO this only applies when a^b can be evaluated to a number (NumericQ)
        if (list.count { it is Power } > 1) {
            val groups = list.filterIsInstance<Power>()
                .filter { it.args.all { arg -> arg is NumberExpr } }
                .groupBy { it[1] }

            if (groups.isNotEmpty()) {
                list.removeIf { it is Power }
                for (group in groups) {
                    if (group.value.size > 1) {
                        val t = Times(*group.value.map { it[0] }.toTypedArray())
                        val power = Power(t, group.key)
                        list.add(power.eval())
                    } else
                        list.add(group.value[0])
                }
            }
        }

        // Factor non-numeric duplicates
        // x * x   = x^2
        // x * x^2 = x^3
        val nonNumerics = list.filter { it !is NumberExpr }
        val groups = nonNumerics.groupBy { if (it is Power) it.args[0] else it }
        for (group in groups) {
            if(group.value.size > 1) {
                list.removeAll { it == group.value[0] }
                list.removeAll { it is Power && it.args[0] == group.value[0] }
                val sum = group.value.map { if (it is Power) it.args[1] else Integer.ONE }
                list.add(Power(group.value[0], Plus(*sum.toTypedArray()).eval()))
            }
        }

        // TODO if infinity zero doesn't work
        val numberItems = list.filterIsInstance<NumberExpr>()
        if (numberItems.contains(Integer.ZERO)) // If zero is an integer return integer/infinite precision rather than double/etc
            return Integer.ZERO

        list.removeIf { it is NumberExpr }
        val product = numberItems.fold(Integer.ONE as NumberExpr) { acc, n -> acc * n }
        if(!product.isOne || list.size == 0)
            list.add(0, product)

        if (list.size == 1)
            return list[0]

        // TODO these should be collapsed in the numerics
        // TODO test cases for multiple infinity expressions
        if (list.contains(ComplexInfinity()))
            return ComplexInfinity()
        if (list.any { it is Infinity }) {
            val infinity = list.first { it is Infinity } as Infinity
            val num = list.firstOrNull { it is NumberExpr && it !is Complex }
            if (num != null) {
                num as NumberExpr
                return Infinity(infinity.direction * if (num.isNegative) -1 else 1)
            }
        }

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
