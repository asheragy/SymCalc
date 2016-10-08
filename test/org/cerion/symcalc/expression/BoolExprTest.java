package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class BoolExprTest {

    @Test
    public void equals() {
        BoolExpr e1 = BoolExpr.TRUE;
        BoolExpr e2 = BoolExpr.FALSE;

        // Different
        assertNotEquals(e1, e2);
        assertNotEquals(e2, e1);

        // Same
        assertEquals(e1, e1);
        assertEquals(e2, e2);

        // Different object type
        assertNotEquals(e1, new IntegerNum(5));
        assertNotEquals(e1, new ListExpr());
    }

}