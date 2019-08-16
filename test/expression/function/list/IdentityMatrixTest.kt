package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.Integer
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
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Integer.ZERO).eval().type)

        // Negative
        Assert.assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Integer(-10)).eval().type)
    }

    @Test
    fun basic() {
        val e = IdentityMatrix(Integer(3))
        val expected = ListExpr(
                ListExpr(Integer.ONE, Integer.ZERO, Integer.ZERO),
                ListExpr(Integer.ZERO, Integer.ONE, Integer.ZERO),
                ListExpr(Integer.ZERO, Integer.ZERO, Integer.ONE)
        )

        Assert.assertEquals(expected, e.eval())
    }
}