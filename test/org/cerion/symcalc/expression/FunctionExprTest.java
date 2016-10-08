package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Subtract;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionExprTest {
    @Test
    public void equals() {
        FunctionExpr f1 = new Plus(new IntegerNum(3), new RealNum(5.0));
        FunctionExpr f2 = new Subtract(new IntegerNum(3), new RealNum(5.0));
        FunctionExpr f3 = new Plus(new IntegerNum(4), new RealNum(5.0));
        FunctionExpr f4 = new Plus(new IntegerNum(3), new RealNum(5.0)); // same as f1

        assertNotEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f2, f3);
        assertEquals(f1, f4);
    }

}