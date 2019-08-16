package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.Assert.assertEquals
import org.junit.Test

class PartitionTest {

    @Test
    fun validate() {

    }

    @Test
    fun basicTest() {
        // Same length
        val list = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO, Integer(3))
        var e = Partition(list, Integer(4)).eval()
        assertEquals(ListExpr(list), e)

        // Split in half
        e = Partition(list, Integer.TWO).eval()

        val expected = ListExpr(
                ListExpr(Integer.ZERO, Integer.ONE),
                ListExpr(Integer.TWO, Integer(3)))

        assertEquals(expected, e)
    }

    @Test
    fun listTooShort() {
        // Simpler version of next test where the first sublist is too short

        val list = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO)
        val e = Partition(list, Integer(4)).eval()

        val expected = ListExpr()
        assertEquals(expected, e)
    }

    @Test
    fun truncateExtraItems() {
        val list = ListExpr(Integer.ZERO, Integer.ONE, Integer.TWO, Integer(3), Integer(4))
        val e = Partition(list, Integer(2)).eval()

        val expected = ListExpr(
                ListExpr(Integer.ZERO, Integer.ONE),
                ListExpr(Integer.TWO, Integer(3)))

        assertEquals(expected, e)
    }
}