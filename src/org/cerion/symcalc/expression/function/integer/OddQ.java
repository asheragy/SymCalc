package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class OddQ extends FunctionExpr {

    public OddQ(Expr ...e) {
        super(FunctionType.ODDQ, e);
    }

    @Override
    protected Expr evaluate() {

        if(!get(0).isInteger())
            return BoolExpr.FALSE;

        IntegerNum N = (IntegerNum)get(0);
        if(N.IsOdd())
            return BoolExpr.TRUE;

        return BoolExpr.FALSE;
    }

    @Override
    protected ErrorExpr validate() {

        if(size() != 1)
            return invalidParameterCountError(1, size());

        return null;
    }
}