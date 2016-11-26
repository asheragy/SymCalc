package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;

public class StandardDeviation extends FunctionExpr {

    public StandardDeviation(Expr... e) {
        super(FunctionType.STANDARD_DEVIATION,e);
    }

    @Override
    protected Expr evaluate() {
        return null;
    }
}
