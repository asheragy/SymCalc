package org.cerion.symcalc;

import org.cerion.symcalc.expression.Expr;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private static final int SYSTEM_DECIMAL_PRECISION = -1; // numbers evaluated to whatever primitive types hold

    //Table of variables, x = expr
    private Map<String, Expr> mVars = new HashMap<>();

    //Table of function definitions
    private static Map<String, UserFunction> mFunctions;

    public boolean skipEval = true;

    //Decimal evaluation precision
    private boolean mEvalNumber = false;
    private int mDecimalPrecision = SYSTEM_DECIMAL_PRECISION;

    public void setVar(String name, Expr value) {
        mVars.put(name,value);
    }

    public Expr getVar(String name) {
        if(mVars == null)
            return null;

        return mVars.get(name);
    }

    public void setNumericalEval(boolean bEval) {
        setNumericalEval(bEval, SYSTEM_DECIMAL_PRECISION);
    }

    public void setNumericalEval(boolean bEval, int digits) {
        mEvalNumber = bEval;
        mDecimalPrecision = (digits >=0 ? digits : SYSTEM_DECIMAL_PRECISION);
    }

    public boolean isNumericalEval() {
        return mEvalNumber;
    }

}
