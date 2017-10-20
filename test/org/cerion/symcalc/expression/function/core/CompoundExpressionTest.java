package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompoundExpressionTest {

    @Test
    public void variableScope() {
        Expr e1 = new Set(new VarExpr("x"), new IntegerNum(5));
        Expr e2 = new Plus(new IntegerNum(5), new VarExpr("x"));

        Expr e = new CompoundExpression(e2, e1, e2);
        assertEquals(new IntegerNum(10), e.eval());
    }
}