package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;

public class NumberQ extends FunctionExpr {

    public NumberQ(Expr... e) {
        super(Function.NUMBERQ, e);
    }

    @Override
    protected Expr evaluate() {
        return new BoolExpr(get(0).isNumber());
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
