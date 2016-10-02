package org.cerion.symcalc;

import org.cerion.symcalc.expression.Expr;

import java.util.Map;

public class Environment {

    //Table of variables, x = expr
    private Map<String, Expr> mVars;

    //Table of function definitions
    private Map<String, UserFunction> mFunctions;

    //Decimal evaluation precision
    private int mDecimal = -1;
}
