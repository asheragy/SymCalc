package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Partition extends FunctionExpr {

    public Partition(Expr... e) {
        super(FunctionType.PARTITION,e);
    }

    @Override
    protected Expr evaluate() {

        ListExpr input = (ListExpr) get(0);
        int N = ((IntegerNum)get(1)).toInteger();
        ListExpr result = new ListExpr();

        for(int i = 0; i < (input.size() / N); i++) {
            ListExpr sublist = new ListExpr();

            for(int j = 0; j < N; j++) {
                int pos = (i * N) + j;
                if(pos >= input.size())
                    break;

                sublist.add(input.get(pos));
            }

            result.add(sublist);
        }

        return result;
    }

    @Override
    protected ErrorExpr validate() {

        // TODO make generic parameter errors so they can be changed if needed

        if(size() < 2) {
            return new ErrorExpr("missing parameter {2} length");
        } else if (!get(1).isInteger()) {
            return new ErrorExpr("parameter {2} must be an integer");
        } else if (!get(0).isList()) {
            return new ErrorExpr("parameter {1} must be a list");
        }

        return null;
    }

}
