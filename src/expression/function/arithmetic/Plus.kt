package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import java.util.*

class Plus(vararg e: Expr) : FunctionExpr(Function.PLUS, *e) {

    override val properties: Int
        get() = Properties.Flat.value or Properties.NumericFunction.value or Properties.Orderless.value

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
            for (times in timesArg) {
                while (list.contains(times[1])) {
                    times[0] = Plus(times[0], Integer.ONE).eval()
                    list.remove(times[1])
                }
            }

            //If only 1 entry just return it
            return if (list.size == 1) list[0] else Plus(*list.toTypedArray())
        }

        return sum
    }

    operator fun plusAssign(e: Expr) = add(e)

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
