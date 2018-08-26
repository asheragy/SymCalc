package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;

public class CompoundExpression extends FunctionExpr {

    public CompoundExpression(Expr... e) {
        super(FunctionType.COMPOUND_EXPRESSION, e);
    }

    @Override
    protected Expr evaluate() {
        getAll(); // eval all
        return get(size() - 1);
    }
}
