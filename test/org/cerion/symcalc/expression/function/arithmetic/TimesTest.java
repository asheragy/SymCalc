package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.FunctionExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.trig.Sin;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimesTest {

    @Test
    public void timesOne() {
        // 1 * Sin(x) = Sin(x)
        Expr sinx = new Sin(new VarExpr("x"));
        assertEquals(sinx, new Times(IntegerNum.ONE, sinx).eval());
    }

    @Test
    public void doubleEval() {
        Expr e = new Times(IntegerNum.TWO, IntegerNum.TWO);
        assertEquals(new IntegerNum(4), e.eval());
        assertEquals(new IntegerNum(4), e.eval());
    }

    @Test
    public void associativeProperty() {
        FunctionExpr inner = new Times(new VarExpr("x"), new VarExpr("y"));
        FunctionExpr outer = new Times(new VarExpr("z"), inner);

        Expr e = outer.eval();
        assertTrue(e.isFunction("times"));
        assertEquals(3, e.size());
    }
}