package org.cerion.symcalc.expression.function.plots;

import org.cerion.symcalc.UserFunction;
import org.cerion.symcalc.expression.*;
import org.cerion.symcalc.expression.graphics.PlotGraphics;

public class Plot extends FunctionExpr {

    public Plot(Expr...e) {
        super(FunctionType.PLOT, e);
    }

    @Override
    protected Expr evaluate() {
        Expr e = get(0);
        ListExpr list = getList(1);

        UserFunction function = new UserFunction("_", e, (VarExpr)list.get(0));

        return new PlotGraphics(function, list.get(1).toIntegerNum(), list.get(2).toIntegerNum());
    }

    @Override
    protected ErrorExpr validate() {
        if (size() == 0)
            return ErrorExpr.getInvalidParameterType(Expr.class, 0);

        if (size() < 2 || !get(1).isList())
            return ErrorExpr.getInvalidParameterType(ListExpr.class, 1);

        // TODO add more

        return null;
    }
}
