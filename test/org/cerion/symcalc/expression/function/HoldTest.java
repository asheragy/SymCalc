package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HoldTest {

    @Test
    public void basicEval() {
        Expr e = new Hold(new Plus(IntegerNum.ONE, IntegerNum.ONE));
        e = e.eval();

        assertTrue(e.isFunction("hold"));
        assertEquals(1, e.size());
        assertTrue(e.get(0).isFunction("plus"));
    }

    @Test
    public void varExprNotEvaluated() {
        Expr e = new Hold(new Plus(new VarExpr("x"), new VarExpr("x")));
        e.getEnv().setVar("x", new IntegerNum(5));

        // Verify plus is not evaluated and is x+x, not 5+5
        e = e.eval().get(0).get(0);
        assertEquals(new VarExpr("x"), e);
    }
}