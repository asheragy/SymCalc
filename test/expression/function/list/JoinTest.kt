package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JoinTest {

    @Test
    fun validate() {
        val e1 = ListExpr()
        val e2 = Integer.TWO
        val e3 = ListExpr(Integer.ZERO, Integer.ONE)

        assertTrue(Join(e1, e2).eval().isError)
        assertTrue(Join(e2).eval().isError)
        assertTrue(Join(e2, e3).eval().isError)
    }

    @Test
    fun emptyList() {
        val e = ListExpr()
        assertEquals(e, Join(e).eval())
        assertEquals(e, Join(e, e).eval())
    }

    @Test
    fun basicTest() {
        val e = Join(L1, L2).eval()

        val expected = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO)
        assertEquals(expected, e)
    }

    companion object {
        private val L1 = ListExpr(Integer.ZERO, Integer.ONE)
        private val L2 = ListExpr(Integer.TWO)
    }
}