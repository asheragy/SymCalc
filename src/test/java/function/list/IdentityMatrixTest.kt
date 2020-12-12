package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentityMatrixTest {

    @Test
    fun validate() {
        // Non-integers
        assertEquals(Expr.ExprType.ERROR, IdentityMatrix(RealDouble(2.34)).eval().type)
        assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Pi()).eval().type)

        // Zero
        assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Integer.ZERO).eval().type)

        // Negative
        assertEquals(Expr.ExprType.ERROR, IdentityMatrix(Integer(-10)).eval().type)
    }

    @Test
    fun basic() {
        val e = IdentityMatrix(Integer(3))
        val expected = ListExpr(
                ListExpr(Integer.ONE, Integer.ZERO, Integer.ZERO),
                ListExpr(Integer.ZERO, Integer.ONE, Integer.ZERO),
                ListExpr(Integer.ZERO, Integer.ZERO, Integer.ONE)
        )

        assertEquals(expected, e.eval())
    }
}