package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

public class VectorQ extends FunctionExpr {

    public VectorQ(Expr... e) {
        super(FunctionType.VECTORQ,e);
    }

    @Override
    protected Expr evaluate() {
        if (!get(0).isList())
            return BoolExpr.FALSE;

        ListExpr list = getList(0);
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).isList())
                return BoolExpr.FALSE;
        }

        return BoolExpr.TRUE;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
