package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.Hold

class Select(vararg e: Expr) : FunctionExpr(Function.SELECT, *e) {

    // TODO this is a workaround until 2nd parameter can be passed in differently for function copy
    override val properties: Int
        get() = Expr.Properties.HOLD.value

    private val functionName get() = (args[1] as FunctionExpr).name

    override fun evaluate(): Expr {
        val list = this[0].asList().eval()
        val result = ListExpr()

        for (i in 0 until list.size) {
            var e: Expr? = createFunction(functionName, list[i])
            e = e!!.eval()

            if (e.equals(BoolExpr.TRUE)) {
                result.add(list[i])
            }
        }

        return result
    }

    override fun validate() {
        validateParameterType(0, ExprType.LIST)
        validateParameterType(1, ExprType.FUNCTION)
    }
}
