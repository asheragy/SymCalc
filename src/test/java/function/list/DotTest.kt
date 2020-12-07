package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DotTest {

    @Test
    fun validate() {
        // Too few parameters
        assertTrue(Dot(ListExpr(Integer(1))).eval().isError)

        // Too many
        assertTrue(Dot(ListExpr(Integer(1)), ListExpr(Integer(2)), Integer(3)).eval().isError)

        // Not a list
        assertTrue(Dot(ListExpr(Integer(1)), Integer(2)).eval().isError)

        // Vectors of unequal length
        assertTrue(Dot(ListExpr(Integer(1)), ListExpr(Integer(2), Integer(3))).eval().isError, "Vectors must be same length")

        val vector2 = ListExpr(Integer(1), Integer(2))
        val vector3 = ListExpr(Integer(1), Integer(2), Integer(3))
        val matrix23 = ListExpr(
                ListExpr(Integer(1), Integer(2), Integer(3)),
                ListExpr(Integer(4), Integer(5), Integer(6)))

        // Vector and matrix
        assertTrue(Dot(vector2, matrix23).eval().isError, "Arrays must be same rank (1)")
        assertTrue(Dot(vector3, matrix23).eval().isError, "Arrays must be same rank (2)")

        // Matrix of incompatible sizes
        assertTrue(Dot(matrix23, matrix23).eval().isError, "Matrix of incompatible size")
    }

    @Test
    fun vector() {
        val e = Dot(
                ListExpr(Integer(2), Integer(3)),
                ListExpr(Integer(4), Integer(5)))

        assertEquals(Integer(23), e.eval())
    }

    @Test
    fun matrix() {
        val e = Dot(
                ListExpr(
                        ListExpr(1, 2, 3),
                        ListExpr(4, 5, 6)),
                ListExpr(
                        ListExpr(7, 8),
                        ListExpr(9, 10),
                        ListExpr(11, 12)
                ))

        val expected = ListExpr(
                ListExpr(58, 64),
                ListExpr(139, 154)
        )

        assertEquals(expected, e.eval())
    }
}