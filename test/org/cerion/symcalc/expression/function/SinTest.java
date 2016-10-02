package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class SinTest {

    @Test
    public void delayEval() {
        FunctionExpr sin = new Sin(new IntegerNum(5));
        Expr eval = sin.eval();

        // Eval does nothing
        assertEquals(sin, eval);

        // Evals to number
        FunctionExpr sin2 = new N(sin);
        eval = sin2.eval();
        assertTrue(eval.isNumber());

        RealNum num = (RealNum)eval;
        assertEquals(-0.958924, num.toDouble(), 0.00001);
    }
}