package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import java.util.*

class RandomInteger(vararg e: Expr) : FunctionExpr(*e) {

    constructor(max: Int) : this(Integer(max))

    // TODO_LP Research more, input may be bit length or something different than implementation

    override fun evaluate(): Expr {

        if (size == 0) { //Default no parameters is random 0 or 1
            return Integer(getRandomInteger(0, 1))
        } else if (get(0).isInteger) {
            val N = (get(0) as Integer).intValue()
            return Integer(getRandomInteger(0, N))
        } else if (get(0) is ListExpr) {
            val min = getList(0).getInteger(0).intValue()
            val max = getList(0).getInteger(1).intValue()
            return Integer(getRandomInteger(min, max))
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