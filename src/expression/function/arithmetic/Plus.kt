package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import java.util.*

class Plus(vararg e: Expr) : FunctionExpr(Function.PLUS, *e) {

    override val properties: Int
        get() = Properties.ASSOCIATIVE.value or Properties.NumericFunction.value or Properties.Orderless.value

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

        // TODO try extracting numbers via filter
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
            val groups = list.groupBy { it.toString() }
            for (group in groups) {
                if (group.value.size > 1) {
                    list.removeIf { it.toString() == group.key }
                    list.add(Times(IntegerNum(group.value.size), group.value.first()))
                }
            }

            // See if any elements can be added to a Times()
            for (arg in list) {
                if (arg is Times && arg.size == 2 && arg[1] !is NumberExpr) {
                    while (list.contains(arg[1])) {
                        arg[0] = Plus(arg[0], IntegerNum.ONE).eval()
                        list.remove(arg[1])
                    }
                    // TODO continuing this loop after removing elements causes error, rewrite to fix this so it can work multiple times
                    break
                }
            }

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
