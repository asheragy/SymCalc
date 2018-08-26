package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class EvenQ extends FunctionExpr {

    public EvenQ(Expr ...e) {
        super(FunctionType.EVENQ, e);
    }

    @Override
    protected Expr evaluate() {

        if(!get(0).isInteger())
            return BoolExpr.FALSE;

        IntegerNum N = (IntegerNum)get(0);
        if(N.IsEven())
            return BoolExpr.TRUE;

        return BoolExpr.FALSE;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
