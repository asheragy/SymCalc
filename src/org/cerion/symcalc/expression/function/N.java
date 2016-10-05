package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class N extends FunctionExpr {

    public N(Expr... e) {
        super(FunctionType.N,e);
    }

    @Override
    protected Expr evaluate() {
        if(size() > 0) {
            if(size() > 1 && get(1).isInteger()) {
                IntegerNum n = (IntegerNum)get(1);
                getEnv().setNumericalEval(true, n.toInteger());
            } else
                getEnv().setNumericalEval(true);

            return get(0).eval();
        }

        return this;
    }
}
