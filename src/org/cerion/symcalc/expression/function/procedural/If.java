package org.cerion.symcalc.expression.function.procedural;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;

public class If extends FunctionExpr {

    public If(Expr... e) {
        super(FunctionType.IF, e);
    }

    @Override
    protected Expr evaluate() {
        Expr p0 = get(0);
        if (p0.isBool()) {
            if (p0.asBool() == BoolExpr.TRUE)
                return get(1);

            return get(2);
        }

        return this;
    }
}
