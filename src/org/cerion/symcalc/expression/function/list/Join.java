package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

import java.util.List;

public class Join extends FunctionExpr {

    public Join(Expr... e) {
        super(FunctionType.JOIN,e);
    }

    @Override
    protected Expr evaluate() {
        ListExpr result = new ListExpr();

        for(int i = 0; i < size(); i++) {
            ListExpr e = (ListExpr)get(i);
            result.addAll(e.getArgs());
        }

        return result;
    }

    @Override
    protected ErrorExpr validate() {

        for(int i = 0; i < size(); i++) {
            Expr e = get(i);
            if(!e.isList()) {
                return ErrorExpr.getInvalidParameterType(ListExpr.class, i);
            }
        }

        return null;
    }
}
