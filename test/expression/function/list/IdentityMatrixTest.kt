package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert
import org.junit.Test

class IdentityMatrixTest {

    @Test
    fun validate() {
        // Non-integers
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(RealDouble(2.34)).eval().type)
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Pi()).eval().type)

        // Zero
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(IntegerNum.ZERO).eval().type)

        // Negative
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(IntegerNum(-10)).eval().type)
    }

    @Test
    fun basic() {
        val e = IdentityMatrix(IntegerNum(3))
        val expected = ListExpr(
                ListExpr(IntegerNum.ONE, IntegerNum.ZERO, IntegerNum.ZERO),
                ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.ZERO),
                ListExpr(IntegerNum.ZERO, IntegerNum.ZERO, IntegerNum.ONE)
        )

        Assert.assertEquals(expected, e.eval())
    }
}