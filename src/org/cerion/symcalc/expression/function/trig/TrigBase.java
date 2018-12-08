package org.cerion.symcalc.expression.function.trig;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;
import org.cerion.symcalc.expression.number.NumberExpr;

public abstract class TrigBase extends FunctionExpr {

    protected TrigBase(Function t, Expr... e) {
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
        return Properties.LISTABLE.getValue();
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
