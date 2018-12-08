package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class N extends FunctionExpr {

    public N(Expr... e) {
        super(Function.N,e);
    }

    @Override
    protected Expr evaluate() {
        if(size() > 0) {
            if(size() > 1 && get(1).isInteger()) {
                IntegerNum n = (IntegerNum)get(1);
                getEnv().setNumericalEval(true, n.intValue());
            } else
                getEnv().setNumericalEval(true);

            return get(0).eval();
        }

        return this;
    }
}
