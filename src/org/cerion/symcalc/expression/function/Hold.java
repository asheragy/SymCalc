package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Hold extends FunctionExpr {

    public Hold(Expr... e) {
        super(FunctionType.HOLD,e);
    }

    @Override
    protected Expr evaluate() {
        return this;
    }

    @Override
    protected int getProperties() {
        return Properties.HOLD.value;
    }
}
