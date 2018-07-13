package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.ErrorExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;

public class Flatten extends FunctionExpr {

    public Flatten(Expr... e) {
        super(FunctionType.FLATTEN,e);
    }

    @Override
    protected Expr evaluate() {

        ListExpr l = (ListExpr)get(0);
        ListExpr result = new ListExpr();

        for(int i = 0; i < l.size(); i++) {
            Expr e = l.get(i);

            if(e.isList()) {
                ListExpr sublist = (ListExpr)e;
                sublist = (ListExpr)new Flatten(sublist).eval(); // TODO eval may be redundant since getAll will eval
                result.addAll(sublist.getAll());
            } else {
                result.add(e);
            }
        }

        return result;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterType(0, ExprType.LIST);
    }
}
