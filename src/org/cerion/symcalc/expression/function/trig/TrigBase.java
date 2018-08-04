package org.cerion.symcalc.expression.function.trig;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.RealNum;

public abstract class TrigBase extends FunctionExpr {

    protected TrigBase(FunctionType t, Expr... e) {
        super(t, e);
    }

    protected abstract Expr evaluate(NumberExpr num);

    @Override
    public Expr evaluate() {
        if(get(0).isNumber() && getEnv().isNumericalEval()) {
            NumberExpr num = (NumberExpr)get(0);
            return evaluate(num);
        }

        return this;
    }

    @Override
    protected int getProperties() {
        return Properties.LISTABLE.value;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
