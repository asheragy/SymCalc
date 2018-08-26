package org.cerion.symcalc.expression.function.core;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.number.ComplexNum;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;

public class NumberQTest {

    @Test
    public void validation() {
        Assert.assertTrue(new NumberQ().eval().isError());
        Assert.assertTrue(new NumberQ(new IntegerNum(5), new IntegerNum(5)).eval().isError());
    }

    @Test
    public void basic() {
        Assert.assertEquals(BoolExpr.TRUE, new NumberQ(new IntegerNum(5)).eval());
        Assert.assertEquals(BoolExpr.TRUE, new NumberQ(RealNum.create(2.34)).eval());
        Assert.assertEquals(BoolExpr.TRUE, new NumberQ(new RationalNum(2,3)).eval());
        Assert.assertEquals(BoolExpr.TRUE, new NumberQ(new ComplexNum(5,7)).eval());

        // Constant is not
        Assert.assertEquals(BoolExpr.FALSE, new NumberQ(new Pi()).eval());

        // Variable is not
        Assert.assertEquals(BoolExpr.FALSE, new NumberQ(new VarExpr("x")).eval());

        // Unless assigned to something that is NumberQ=true
        Expr e = new CompoundExpression(
                new Set(new VarExpr("x"), new IntegerNum(5)),
                new NumberQ(new VarExpr("x")));

        Assert.assertEquals(BoolExpr.TRUE, e.eval());
    }
}