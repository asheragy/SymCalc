package org.cerion.symcalc.expression.function.procedural;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.logical.Greater;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class IfTest {

    @Test
    public void basic() {
        Expr e = new If(new Greater(new IntegerNum(3), new IntegerNum(6)), new VarExpr("y"), new VarExpr("n"));
        assertEquals(new VarExpr("n"), e.eval());

        e = new If(new Greater(new IntegerNum(6), new IntegerNum(3)), new VarExpr("y"), new VarExpr("n"));
        assertEquals(new VarExpr("y"), e.eval());
    }
}