package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Function;
import org.cerion.symcalc.expression.function.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.arithmetic.Divide;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Mean extends FunctionExpr {

    public Mean(Expr... e) {
        super(Function.MEAN,e);
    }

    @Override
    protected Expr evaluate() {

        ListExpr list = (ListExpr)get(0);
        Expr result = new Plus(list.args());
        result = new Divide(result, new IntegerNum(list.size()));

        return result.eval();
    }
}
