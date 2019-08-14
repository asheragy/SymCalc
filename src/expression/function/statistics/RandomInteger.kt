package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum

import java.util.Random

class RandomInteger(vararg e: Expr) : FunctionExpr(Function.RANDOM_INTEGER, *e) {

    constructor(max: Int) : this(IntegerNum(max))

    override fun evaluate(): Expr {

        if (size == 0) { //Default no parameters is random 0 or 1
            return IntegerNum(getRandomInteger(0, 1))
        } else if (get(0).isInteger) {
            val N = (get(0) as IntegerNum).intValue()
            return IntegerNum(getRandomInteger(0, N))
        } else if (get(0).isList) {
            val min = getList(0).getInteger(0).intValue()
            val max = getList(0).getInteger(1).intValue()
            return IntegerNum(getRandomInteger(min, max))
        }

        /* Other cases
		[range, n]
		[range, {n1,n2,...}]
		[dist]
		 */

        return this
    }

    private fun getRandomInteger(min: Int, max: Int): Int {
        val rand = Random()
        val diff = max - min

        return rand.nextInt(diff + 1) + min
    }

    override fun validate() {
    }
}