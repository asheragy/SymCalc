package org.cerion.symcalc.expression.function.logical;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreaterTest {

    @Test
    public void basic() {
        assertEquals(BoolExpr.TRUE, new Greater(new IntegerNum(2), new IntegerNum(1)).eval());
        assertEquals(BoolExpr.FALSE, new Greater(new IntegerNum(2), new IntegerNum(2)).eval());
        assertEquals(BoolExpr.TRUE, new Greater(new IntegerNum(3), new IntegerNum(2), new IntegerNum(1)).eval());
        assertEquals(BoolExpr.FALSE, new Greater(new IntegerNum(3), new IntegerNum(1), new IntegerNum(2)).eval());
        assertEquals(BoolExpr.FALSE, new Greater(new IntegerNum(1), new IntegerNum(3), new IntegerNum(2)).eval());
    }

    @Test
    public void simplification() {
        // First element
        // 2>1>x = 1 > x
        Expr e = new Greater(new IntegerNum(2), new IntegerNum(1), new VarExpr("x")).eval();
        assertEquals(new Greater(new IntegerNum(1), new VarExpr("x")), e);

        //Middle
        // x>5>3>1>y = x>5>1>y
        e = new Greater(new VarExpr("x"), new IntegerNum(5), new IntegerNum(3), new IntegerNum(1), new VarExpr("y")).eval();
        assertEquals(new Greater(new VarExpr("x"), new IntegerNum(5), new IntegerNum(1), new VarExpr("y")), e);

        // Last
        // x>2>1 = x>2
        e = new Greater(new VarExpr("x"), new IntegerNum(2), new IntegerNum(1)).eval();
        assertEquals(new Greater(new VarExpr("x"), new IntegerNum(2)), e);
    }
}