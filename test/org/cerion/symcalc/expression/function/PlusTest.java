package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlusTest {

    @Test
    public void basicAddition() {
        Expr e = new Plus(IntegerNum.ONE, IntegerNum.ONE);
        assertEquals(IntegerNum.TWO, e.eval());
    }

    @Test
    public void nestedInstancesMerge() {
        FunctionExpr inner = new Plus(new VarExpr("x"), new VarExpr("y"));
        FunctionExpr outer = new Plus(new VarExpr("z"), inner);

        Expr e = outer.eval();
        assertTrue(e.isFunction("plus"));
        assertEquals(3, e.size());
    }

    @Test
    public void identityProperty() {
        VarExpr v = new VarExpr("x");
        IntegerNum n = new IntegerNum(0);

        Expr e = new Plus(v,n);
        assertEquals(v, e.eval());

        e = new Plus(n,v);
        assertEquals(v, e.eval());
    }
}