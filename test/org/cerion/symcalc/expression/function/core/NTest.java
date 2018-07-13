package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by andy on 7/11/2018.
 */
public class NTest {

    @Test
    public void constantPlusInteger() {
        Expr e = new N(new Plus(new Pi(), new IntegerNum(5)));
        assertEquals(RealNum.create(8.141592653589793), e.eval());

        // TODO this is what mathematica does but it doesn't seem right
        e = new Plus(new N(new IntegerNum(5)), new Pi());
        assertEquals(RealNum.create(8.141592653589793), e.eval());
    }
}