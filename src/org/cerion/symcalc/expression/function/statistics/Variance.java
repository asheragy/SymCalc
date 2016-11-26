package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.arithmetic.Divide;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Subtract;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Variance extends FunctionExpr {

    public Variance(Expr... e) {
        super(FunctionType.VARIANCE,e);
    }

    @Override
    protected Expr evaluate() {

        ListExpr list = (ListExpr)get(0);
        Expr mean = new Mean(list).eval();

        FunctionExpr sum = new Plus();

        for(int i = 0; i < list.size(); i++) {
            Expr e = list.get(i);
            Expr diff = new Subtract(e, mean).eval(); // TODO may be another function for (a - b)^2
            Expr square = new Times(diff, diff).eval();
            sum.add(square);
        }

        // TODO, divide by N or N-1, both seem to be used
        Expr result = new Divide(sum, new IntegerNum(list.size()));

        return result.eval();
    }
}
