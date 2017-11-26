package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.core.N;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class DivideTest {

    @Test
    public void precisionTest() {
        Expr e = new Divide(new IntegerNum(5), new IntegerNum(3));
        assertEquals("5/3", e.eval().toString());

        e = new N(e).eval();
        assertEquals("1.6666666666666667", e.toString());

        e = new Divide(new IntegerNum(10), new IntegerNum(6));
        e = new N(e).eval();
        assertEquals("1.6666666666666667", e.toString());
    }

    @Test
    public void reduces() {
        Expr e = new Divide(new IntegerNum(10), new IntegerNum(6));
        assertEquals("5/3", e.eval().toString());

        e = new Divide(new IntegerNum(10), new IntegerNum(2));
        assertEquals("5", e.eval().toString());
    }
}