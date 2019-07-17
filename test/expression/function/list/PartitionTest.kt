package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Test

class PartitionTest {

    @Test
    fun validate() {

    }

    @Test
    fun basicTest() {
        // Same length
        val list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, IntegerNum(3))
        var e = Partition(list, IntegerNum(4)).eval()
        assertEquals(ListExpr(list), e)

        // Split in half
        e = Partition(list, IntegerNum.TWO).eval()

        val expected = ListExpr(
                ListExpr(IntegerNum.ZERO, IntegerNum.ONE),
                ListExpr(IntegerNum.TWO, IntegerNum(3)))

        assertEquals(expected, e)
    }

    @Test
    fun listTooShort() {
        // Simpler version of next test where the first sublist is too short

        val list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO)
        val e = Partition(list, IntegerNum(4)).eval()

        val expected = ListExpr()
        assertEquals(expected, e)
    }

    @Test
    fun truncateExtraItems() {
        val list = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, IntegerNum(3), IntegerNum(4))
        val e = Partition(list, IntegerNum(2)).eval()

        val expected = ListExpr(
                ListExpr(IntegerNum.ZERO, IntegerNum.ONE),
                ListExpr(IntegerNum.TWO, IntegerNum(3)))

        assertEquals(expected, e)
    }
}