package org.cerion.symcalc.expression.function.trig;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.core.N;
import org.cerion.symcalc.expression.function.statistics.RandomInteger;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void listParameter() {
        ListExpr params = new ListExpr(IntegerNum.ONE, new VarExpr("x"), new RandomInteger());
        Expr e = new Sin(params).eval();

        assertTrue(e.isList());
        assertTrue(e.get(0).isFunction("sin"));
    }
}