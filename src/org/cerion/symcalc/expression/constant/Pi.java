package org.cerion.symcalc.expression.constant;

import org.cerion.symcalc.expression.ConstExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.number.RealNum;

public class Pi extends ConstExpr {

    @Override
    public String toString() {
        return "Pi";
    }

    @Override
    protected Expr evaluate() {
        if (getEnv().isNumericalEval())
            return RealNum.create(Math.PI);

        return this;
    }
}
