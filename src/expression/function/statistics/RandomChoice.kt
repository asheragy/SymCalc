package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.list.Table
import org.cerion.symcalc.expression.number.IntegerNum

class RandomChoice(vararg e: Expr) : FunctionExpr(Function.RANDOM_CHOICE, *e) {

    override fun evaluate(): Expr {
        val list = get(0) as ListExpr
        if (size > 1) {
            return Table(RandomChoice(list), ListExpr(getInteger(1))).eval()
        }

        val rand = RandomInteger(list.size - 1).eval() as IntegerNum
        return list[rand.intValue()]

        /* Other cases
        [list, {n1, n2,...}]
        [{w1, w2,...} => {e1, e2,...}]
        [wlist -> elist, n]
        [wlist -> elist, {n1, n2,...}]
         */
    }
}
