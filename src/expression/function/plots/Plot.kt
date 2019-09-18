package org.cerion.symcalc.expression.function.plots

import org.cerion.symcalc.UserFunction
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.graphics.PlotGraphics

class Plot(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val list = getList(1)

        val function = UserFunction("_", e, list[0] as VarExpr)

        return PlotGraphics(function, list[1].asInteger(), list[2].asInteger())
    }

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(1, ExprType.LIST)
    }
}
