package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;

public class StandardDeviation extends FunctionExpr {

    public StandardDeviation(Expr... e) {
        super(Function.STANDARD_DEVIATION,e);
    }

    @Override
    protected Expr evaluate() {
        return null;
    }
}
