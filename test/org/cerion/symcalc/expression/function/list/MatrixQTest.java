package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;

public class MatrixQTest {

    @Test
    public void isMatrix() {
        Expr a = new IntegerNum(3);
        Expr b = RealNum.create(6.32);
        Expr c = new VarExpr("x");

        // 0x0
        Assert.assertEquals(BoolExpr.TRUE, new MatrixQ(new ListExpr(new ListExpr())).eval());

        // 1x1
        Assert.assertEquals(BoolExpr.TRUE, new MatrixQ(new ListExpr(new ListExpr(a))).eval());

        // 2x3
        Assert.assertEquals(BoolExpr.TRUE, new MatrixQ(new ListExpr(
                new ListExpr(a,b,c),
                new ListExpr(c,b,a)
        )).eval());
    }

    @Test
    public void isNotMatrix() {
        Expr a = new IntegerNum(3);
        Expr b = RealNum.create(6.32);
        Expr c = new VarExpr("x");

        // Not a list
        Assert.assertEquals(BoolExpr.FALSE, new MatrixQ(a).eval());

        // Not 2 dimension
        Assert.assertEquals(BoolExpr.FALSE, new MatrixQ(new ListExpr(a,b)).eval());

        // More than 2 dimension
        Assert.assertEquals(BoolExpr.FALSE, new MatrixQ(new ListExpr(
                new ListExpr(a,b,c),
                new ListExpr(c,b,new ListExpr(c))
        )).eval());

        // Size does not match
        Assert.assertEquals(BoolExpr.FALSE, new MatrixQ(new ListExpr(
                new ListExpr(a,b,c),
                new ListExpr(c,b)
        )).eval());
    }
}