package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;

public class NumericQ extends FunctionExpr {

    public NumericQ(Expr... e) {
        super(Function.NUMERICQ, e);
    }

    @Override
    protected Expr evaluate() {
        Expr test = get(0);

        if (test.isNumber())
            return BoolExpr.TRUE;

        if (test.isConst())
            return BoolExpr.TRUE;

        if (test.isFunction()) {
            FunctionExpr func = (FunctionExpr)test;
            if (func.isNumeric()) {

                for(int i = 0; i < func.size(); i++) {
                    BoolExpr isNumeric = (BoolExpr)new NumericQ(func.get(i)).eval();
                    if (isNumeric == BoolExpr.FALSE)
                        return BoolExpr.FALSE;
                }

                return BoolExpr.TRUE;
            }
        }

        return BoolExpr.FALSE;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
