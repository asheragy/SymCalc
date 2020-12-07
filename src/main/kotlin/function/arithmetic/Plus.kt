package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import java.util.*

class Plus(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.Flat.value or Properties.NumericFunction.value or Properties.Orderless.value or Properties.LISTABLE.value

    override fun evaluate(): Expr {
        val list = ArrayList<Expr>()
        for (i in 0 until size)
            list.add(get(i))

        // Combine number values
        val numberItems = list.filterIsInstance<NumberExpr>()
        list.removeIf { it is NumberExpr }
        val sum = numberItems.fold(Integer.ZERO as NumberExpr) { acc, n -> acc + n }
        if (!sum.isZero)
            list.add(sum)

        //At least 1 non-number value
        if (list.size > 0) {
            val groups = list.groupBy { it.toString() }
            for (group in groups) {
                if (group.value.size > 1) {
                    list.removeIf { it.toString() == group.key }
                    list.add(Times(Integer(group.value.size), group.value.first()))
                }
            }

            // If element is also contained in Times(), remove it and increment by 1  (2*Pi) + Pi = 3*Pi
            val timesArg = list.filter { it is Times && it.size == 2 && it[0] is NumberExpr && it[1] !is NumberExpr }
            for (i in 0 until timesArg.size) {
                val times = timesArg[i] as Times
                while (list.contains(times[1])) {
                    val t = (times[0] + Integer.ONE) * times[1]
                    list.remove(times)
                    list.remove(times[1])
                    list.add(t)
                }
            }

            //If only 1 entry just return it
            if (list.size == 1)
                return list[0]

            if (list.contains(ComplexInfinity()))
                return ComplexInfinity()

            return Plus(*list.toTypedArray())
        }

        return sum
    }

    override fun toString(): String {
        if (size > 0) {
            val strings = args.map {
                if (it is Times)
                    "($it)"
                else
                    it.toString()
            }

            return strings.joinToString(" + ")
        }

        return super.toString()
    }

    override fun validate() {
    }
}
