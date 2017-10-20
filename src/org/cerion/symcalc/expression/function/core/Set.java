package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.VarExpr;

public class Set extends FunctionExpr {

    public Set(Expr... e) {
        super(FunctionType.SET, e);
    }

    @Override
    protected Expr evaluate() {
        VarExpr v = get(0).toVar();
        Expr e = get(1);

        getEnv().setVar(v.value(), e);

        return e;
    }
}
