package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ErrorExprTest {

    @Test
    public void equals() {
        ErrorExpr e1 = new ErrorExpr("error 1");
        ErrorExpr e2 = new ErrorExpr("error 2");

        // Different
        assertNotEquals(e1, e2);
        assertNotEquals(e1, e1);
    }

    /* TODO not really valid anymore, errors may be handled with exceptions so purposely passing ErrorExpr("test" is wrong
    @Test
    public void singleErrorGetsPassedUp() {
        Expr e = new Plus(IntegerNum.ONE, IntegerNum.TWO, new ErrorExpr("test")).eval();
        assertTrue(e.isError());
    }
    */
}