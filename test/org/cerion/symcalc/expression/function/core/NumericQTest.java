package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class NumericQTest {

    @Test
    public void basic() {
        // Number
        Assert.assertEquals(BoolExpr.TRUE, new NumericQ(new IntegerNum(5)).eval());
        Assert.assertEquals(BoolExpr.TRUE, new NumericQ(RealNum.create(4.576)).eval());

        // Constant
        Assert.assertEquals(BoolExpr.TRUE, new NumericQ(new Pi()).eval());

        // NumericFunction AND all parameters are NumbericQ=true
        Assert.assertEquals(BoolExpr.TRUE, new NumericQ(new Plus(new IntegerNum(2), new IntegerNum(3))).eval());
        Assert.assertEquals(BoolExpr.TRUE, new NumericQ(new Plus(new IntegerNum(2), new Pi())).eval());

        // Variable assigned to number
        Expr e = new CompoundExpression(
                new Set(new VarExpr("x"), new IntegerNum(5)),
                new NumericQ(new VarExpr("x")));

        Assert.assertEquals(BoolExpr.TRUE, e.eval());
    }

    @Test
    public void nonNumeric() {
        // Numeric inside non-numberic
        Assert.assertEquals(BoolExpr.FALSE, new NumericQ(new ListExpr(new IntegerNum(5))).eval());

        // Non-numeric inside numericFunction
        Assert.assertEquals(BoolExpr.FALSE, new NumericQ(new Plus(new IntegerNum(5), new ListExpr())).eval());

        // Variable
        Assert.assertEquals(BoolExpr.FALSE, new NumericQ(new VarExpr("x")).eval());
    }
}