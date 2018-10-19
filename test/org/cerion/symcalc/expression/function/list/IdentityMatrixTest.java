package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Assert;
import org.junit.Test;

public class IdentityMatrixTest {

    @Test
    public void validate() {
        // Non-integers
        Assert.assertEquals(Expr.ExprType.ERROR, new IdentityMatrix(RealNum.Companion.create(2.34)).eval().getType());
        Assert.assertEquals(Expr.ExprType.ERROR, new IdentityMatrix(new Pi()).eval().getType());

        // Zero
        Assert.assertEquals(Expr.ExprType.ERROR, new IdentityMatrix(IntegerNum.ZERO).eval().getType());

        // Negative
        Assert.assertEquals(Expr.ExprType.ERROR, new IdentityMatrix(new IntegerNum(-10)).eval().getType());
    }

    @Test
    public void basic() {
        Expr e = new IdentityMatrix(new IntegerNum(3));
        ListExpr expected = new ListExpr(
                new ListExpr(IntegerNum.ONE, IntegerNum.ZERO, IntegerNum.ZERO),
                new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.ZERO),
                new ListExpr(IntegerNum.ZERO, IntegerNum.ZERO, IntegerNum.ONE)
        );

        Assert.assertEquals(expected, e.eval());
    }
}