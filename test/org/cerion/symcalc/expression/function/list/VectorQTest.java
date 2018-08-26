package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;


public class VectorQTest {

    @Test
    public void isVector() {
        Assert.assertEquals(BoolExpr.TRUE, new VectorQ(new ListExpr()).eval());
        Assert.assertEquals(BoolExpr.TRUE, new VectorQ(new ListExpr(new IntegerNum(3))).eval());
        Assert.assertEquals(BoolExpr.TRUE, new VectorQ(new ListExpr(new IntegerNum(3), RealNum.create(2.35))).eval());
    }

    @Test
    public void isNotVector() {
        // Not a list
        Assert.assertEquals(BoolExpr.FALSE, new VectorQ(new IntegerNum(5)).eval());

        // More than 1 dimension
        Assert.assertEquals(BoolExpr.FALSE, new VectorQ(new ListExpr(new IntegerNum(5), new ListExpr())).eval());
    }
}