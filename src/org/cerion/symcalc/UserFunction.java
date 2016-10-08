package org.cerion.symcalc;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;

public class UserFunction {

    private String mName;
    private Expr mExpr;
    private VarExpr mVars[];

    public UserFunction(String name, Expr e, VarExpr ...vars) {
        mName = name;
        mExpr = e;
        mVars = vars;
    }

    public Expr eval(Expr ...params) {

        if(params.length != mVars.length)
            throw new IllegalArgumentException();

        for(int i = 0; i < mVars.length; i++) {
            mExpr.getEnv().setVar(mVars[i].value(), params[i]);
        }

        return mExpr.eval();
    }

    @Override
    public String toString() {

        String params = "";
        for(int i = 0; i < mVars.length; i++) {
            if(i > 0)
                params += ",";
            params += mVars[i];
        }

        return mName + "[" + params + "] = " + mExpr;
    }
}
