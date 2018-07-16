package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

public class MatrixQ extends FunctionExpr {

    public MatrixQ(Expr... e) {
        super(FunctionType.MATRIXQ,e);
    }

    @Override
    protected Expr evaluate() {
        if (!get(0).isList())
            return BoolExpr.FALSE;

        int size = -1;
        ListExpr list = getList(0);
        for(int i = 0; i < list.size(); i++) {
            // Every sublist must be a vector of the same length
            if (!list.get(i).isList())
                return BoolExpr.FALSE;

            ListExpr sublist = list.getList(i);
            if (new VectorQ(sublist).eval() != BoolExpr.TRUE)
                return BoolExpr.FALSE;

            if(size > 0) {
                if(sublist.size() != size)
                    return BoolExpr.FALSE;
            } else
                size = sublist.size();
        }

        return BoolExpr.TRUE;
    }
}
