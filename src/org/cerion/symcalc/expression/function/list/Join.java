package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
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
            result.addAll(e.getAll());
        }

        return result;
    }

    @Override
    public void validate() throws ValidationException {
        for(int i = 0; i < size(); i++)
            validateParameterType(i, ExprType.LIST);
    }
}
