package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.*;

public class Select extends FunctionExpr {

    private String mFunctionName;

    public Select(Expr... e) {
        super(FunctionType.SELECT,e[0]);

        FunctionExpr f = (FunctionExpr)e[1];
        mFunctionName = f.getName();
    }

    @Override
    protected Expr evaluate() {

        ListExpr list = (ListExpr)get(0);
        ListExpr result = new ListExpr();

        for(int i = 0; i < list.size(); i++) {
            Expr e = FunctionExpr.CreateFunction(mFunctionName, list.get(i));
            e = e.eval();

            if(e.equals(BoolExpr.TRUE)) {
                result.add(list.get(i));
            }
        }

        return result;
    }

}
