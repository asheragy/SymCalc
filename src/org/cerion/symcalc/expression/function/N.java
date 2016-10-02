package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class N extends FunctionExpr {

    public N(Expr... e) {
        super(FunctionType.N,e);
    }

    @Override
    public Expr eval() {

        if(size() > 0) {
            if(size() > 1 && get(1).isInteger()) {
                IntegerNum n = (IntegerNum)get(1);
                Expr.getEnv().setNumericalEval(true, n.toInteger());
            } else
                Expr.getEnv().setNumericalEval(true);

            return get(0).eval();
        }

        return this;
    }
}
