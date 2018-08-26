package org.cerion.symcalc.expression.function.calculus;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Subtract;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.function.trig.*;
import org.cerion.symcalc.expression.number.ComplexNum;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;


public class DTest {

    @Test
    public void single() {
        // Numbers
        Assert.assertEquals(IntegerNum.ZERO, new D(IntegerNum.TWO, new VarExpr("x")).eval());
        Assert.assertEquals(IntegerNum.ZERO, new D(RealNum.create(2.354), new VarExpr("x")).eval());
        Assert.assertEquals(IntegerNum.ZERO, new D(new RationalNum(4,6), new VarExpr("x")).eval());
        Assert.assertEquals(IntegerNum.ZERO, new D(ComplexNum.ZERO, new VarExpr("x")).eval());

        // Constant
        Assert.assertEquals(IntegerNum.ZERO, new D(new Pi(), new VarExpr("x")).eval());

        // D(x,x) = 1
        Assert.assertEquals(IntegerNum.ONE, new D(new VarExpr("x"), new VarExpr("x")).eval());

        // D(x,y) = 0
        Assert.assertEquals(IntegerNum.ZERO, new D(new VarExpr("y"), new VarExpr("x")).eval());
    }

    @Test
    public void basic() {
        // D(1 + x) = 1
        Assert.assertEquals(IntegerNum.ONE,
                new D(new Plus(new VarExpr("x"), new VarExpr("y"), new IntegerNum(5)), new VarExpr("x")).eval());

        // D(x - 5) = 1
        Assert.assertEquals(IntegerNum.ONE,
                new D(new Subtract(new VarExpr("x"), new IntegerNum(5)), new VarExpr("x")).eval());
    }

    @Test
    public void trig() {
        // D(Sin(x)) = Cos(x)
        Expr expected = new Cos(new VarExpr("x"));
        Expr actual = new D(new Sin(new VarExpr("x")), new VarExpr("x")).eval();
        Assert.assertEquals(expected, actual);

        // D(Cos(x)) = -Sin(x)
        expected = new Times(new IntegerNum(-1), new Sin(new VarExpr("x")));
        actual = new D(new Cos(new VarExpr("x")), new VarExpr("x")).eval();
        Assert.assertEquals(expected, actual);
    }
}