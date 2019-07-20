package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Select(vararg e: Expr) : FunctionExpr(Function.SELECT, e[0]) {

    private val mFunctionName: String

    init {
        val f = e[1] as FunctionExpr
        mFunctionName = f.name
    }

    override fun evaluate(): Expr {
        val list = get(0) as ListExpr
        val result = ListExpr()

        for (i in 0 until list.size) {
            var e: Expr? = createFunction(mFunctionName, list[i])
            e = e!!.eval()

            if (e.equals(BoolExpr.TRUE)) {
                result.add(list[i])
            }
        }

        return result
    }

}
