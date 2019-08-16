package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert
import org.junit.Test

class DotTest {

    @Test
    fun validate() {
        // Too few parameters
        Assert.assertTrue(Dot(ListExpr(Integer(1))).eval().isError)

        // Too many
        Assert.assertTrue(Dot(ListExpr(Integer(1)), ListExpr(Integer(2)), Integer(3)).eval().isError)

        // Not a list
        Assert.assertTrue(Dot(ListExpr(Integer(1)), Integer(2)).eval().isError)

        // Vectors of unequal length
        Assert.assertTrue("Vectors must be same length", Dot(ListExpr(Integer(1)), ListExpr(Integer(2), Integer(3))).eval().isError)

        val vector2 = ListExpr(Integer(1), Integer(2))
        val vector3 = ListExpr(Integer(1), Integer(2), Integer(3))
        val matrix23 = ListExpr(
                ListExpr(Integer(1), Integer(2), Integer(3)),
                ListExpr(Integer(4), Integer(5), Integer(6)))

        // Vector and matrix
        Assert.assertTrue("Arrays must be same rank (1)", Dot(vector2, matrix23).eval().isError)
        Assert.assertTrue("Arrays must be same rank (2)", Dot(vector3, matrix23).eval().isError)

        // Matrix of incompatible sizes
        Assert.assertTrue("Matrix of incompatible size", Dot(matrix23, matrix23).eval().isError)
    }

    @Test
    fun vector() {
        val e = Dot(
                ListExpr(Integer(2), Integer(3)),
                ListExpr(Integer(4), Integer(5)))

        Assert.assertEquals(Integer(23), e.eval())
    }

    @Test
    fun matrix() {
        val e = Dot(
                ListExpr(
                        ListExpr(Integer(1), Integer(2), Integer(3)),
                        ListExpr(Integer(4), Integer(5), Integer(6))),
                ListExpr(
                        ListExpr(Integer(7), Integer(8)),
                        ListExpr(Integer(9), Integer(10)),
                        ListExpr(Integer(11), Integer(12))
                ))

        val expected = ListExpr(
                ListExpr(Integer(58), Integer(64)),
                ListExpr(Integer(139), Integer(154))
        )

        Assert.assertEquals(expected, e.eval())
    }
}