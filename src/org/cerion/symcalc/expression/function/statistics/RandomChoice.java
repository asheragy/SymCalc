package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.list.Table;
import org.cerion.symcalc.expression.number.IntegerNum;

public class RandomChoice extends FunctionExpr {

    public RandomChoice(Expr... e) {
        super(FunctionType.RANDOM_CHOICE,e);
    }

    @Override
    protected Expr evaluate() {
        ListExpr list = (ListExpr)get(0);
        if (size() > 1) {
            return new Table(new RandomChoice(list), new ListExpr(getInteger(1))).eval();
        }

        IntegerNum rand = (IntegerNum)new RandomInteger(list.size()-1).eval();
        return list.get(rand.intValue());

        /* Other cases
        [list, {n1, n2,...}]
        [{w1, w2,...} => {e1, e2,...}]
        [wlist -> elist, n]
        [wlist -> elist, {n1, n2,...}]
         */
    }
}
