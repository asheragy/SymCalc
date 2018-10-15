package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class FactorialTest {

    @Test
    public void basic() {
        assertEquals(new IntegerNum(1), new Factorial(new IntegerNum(0)).eval());
        assertEquals(new IntegerNum(1), new Factorial(new IntegerNum(1)).eval());
        assertEquals(new IntegerNum(2), new Factorial(new IntegerNum(2)).eval());
        assertEquals(new IntegerNum(720), new Factorial(new IntegerNum(6)).eval());
    }
}