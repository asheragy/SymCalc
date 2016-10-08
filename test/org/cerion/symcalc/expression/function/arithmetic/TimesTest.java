package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimesTest {

    @Test
    public void associativeProperty() {
        FunctionExpr inner = new Times(new VarExpr("x"), new VarExpr("y"));
        FunctionExpr outer = new Times(new VarExpr("z"), inner);

        Expr e = outer.eval();
        assertTrue(e.isFunction("times"));
        assertEquals(3, e.size());
    }
}