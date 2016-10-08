package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class VarExprTest {
    @Test
    public void equals() {
        VarExpr v1 = new VarExpr("x");
        VarExpr v2 = new VarExpr("y");
        VarExpr v3 = new VarExpr("x");

        assertNotEquals(v1, v2);
        assertNotEquals(v1, new IntegerNum(5));
        assertEquals(v1, v3);
    }

}