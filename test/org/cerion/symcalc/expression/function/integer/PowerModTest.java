package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PowerModTest {

    @Test
    public void basic() {
        IntegerNum a = new IntegerNum("1234567890987654321");
        IntegerNum b = new IntegerNum("1122334455667788990");
        IntegerNum m = new IntegerNum("5555555555555555555");

        Expr e = new PowerMod(a,b,m);
        assertEquals(new IntegerNum("498317946897227631"), e.eval());
    }
}