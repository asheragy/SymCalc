package org.cerion.symcalc.expression.function.plots

import org.cerion.symcalc.UserFunction
import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.graphics.PlotGraphics

class Plot(vararg e: Expr) : FunctionExpr(Function.PLOT, *e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val list = getList(1)

        val function = UserFunction("_", e, list[0] as VarExpr)

        return PlotGraphics(function, list[1].asInteger(), list[2].asInteger())
    }

    /*
    @Override
    protected ErrorExpr validate() {
        if (size() == 0)
            return ErrorExpr.getInvalidParameterType(Expr.class, 0);

        if (size() < 2 || !get(1).isList())
            return ErrorExpr.getInvalidParameterType(ListExpr.class, 1);

        // TODO add more

        return null;
    }
    */

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(1, ExprType.LIST)
    }
}
