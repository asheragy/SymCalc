package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;

public class Tally extends FunctionExpr {

    public Tally(Expr... e) {
        super(FunctionType.TALLY,e);
    }

    @Override
    protected Expr evaluate() {
        ListExpr result = new ListExpr();

        ListExpr list = getList(0);
        for(int i = 0; i < list.size(); i++) {
            Expr e = list.get(i);

            boolean found = false;
            for(int j = 0; j < result.size(); j++) {
                Expr keyval = result.get(j);
                Expr key = keyval.get(0);
                IntegerNum val = keyval.get(1).asInteger();

                if(e.equals(key)) {
                    val = new Plus(val, IntegerNum.ONE).eval().asInteger();
                    keyval = new ListExpr(e, val);
                    result.set(j, keyval);
                    found = true;
                    break;
                }
            }

            if (!found)
                result.add(new ListExpr(e, IntegerNum.ONE));
        }

        return result;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
        validateParameterType(0, ExprType.LIST);
    }
}
