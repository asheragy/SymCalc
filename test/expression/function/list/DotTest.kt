package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert
import org.junit.Test

class DotTest {

    @Test
    fun validate() {
        // Too few parameters
        Assert.assertTrue(Dot(ListExpr(IntegerNum(1))).eval().isError)

        // Too many
        Assert.assertTrue(Dot(ListExpr(IntegerNum(1)), ListExpr(IntegerNum(2)), IntegerNum(3)).eval().isError)

        // Not a list
        Assert.assertTrue(Dot(ListExpr(IntegerNum(1)), IntegerNum(2)).eval().isError)

        // Vectors of unequal length
        Assert.assertTrue("Vectors must be same length", Dot(ListExpr(IntegerNum(1)), ListExpr(IntegerNum(2), IntegerNum(3))).eval().isError)

        val vector2 = ListExpr(IntegerNum(1), IntegerNum(2))
        val vector3 = ListExpr(IntegerNum(1), IntegerNum(2), IntegerNum(3))
        val matrix23 = ListExpr(
                ListExpr(IntegerNum(1), IntegerNum(2), IntegerNum(3)),
                ListExpr(IntegerNum(4), IntegerNum(5), IntegerNum(6)))

        // Vector and matrix
        Assert.assertTrue("Arrays must be same rank (1)", Dot(vector2, matrix23).eval().isError)
        Assert.assertTrue("Arrays must be same rank (2)", Dot(vector3, matrix23).eval().isError)

        // Matrix of incompatible sizes
        Assert.assertTrue("Matrix of incompatible size", Dot(matrix23, matrix23).eval().isError)
    }

    @Test
    fun vector() {
        val e = Dot(
                ListExpr(IntegerNum(2), IntegerNum(3)),
                ListExpr(IntegerNum(4), IntegerNum(5)))

        Assert.assertEquals(IntegerNum(23), e.eval())
    }

    @Test
    fun matrix() {
        val e = Dot(
                ListExpr(
                        ListExpr(IntegerNum(1), IntegerNum(2), IntegerNum(3)),
                        ListExpr(IntegerNum(4), IntegerNum(5), IntegerNum(6))),
                ListExpr(
                        ListExpr(IntegerNum(7), IntegerNum(8)),
                        ListExpr(IntegerNum(9), IntegerNum(10)),
                        ListExpr(IntegerNum(11), IntegerNum(12))
                ))

        val expected = ListExpr(
                ListExpr(IntegerNum(58), IntegerNum(64)),
                ListExpr(IntegerNum(139), IntegerNum(154))
        )

        Assert.assertEquals(expected, e.eval())
    }
}