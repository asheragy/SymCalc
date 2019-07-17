package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Test

class TallyTest {

    @Test
    fun basic() {
        assertEquals(ListExpr(ListExpr(VarExpr("a"), IntegerNum(3))),
                Tally(ListExpr(VarExpr("a"), VarExpr("a"), VarExpr("a"))).eval())

        val expected = ListExpr(ListExpr(IntegerNum(5), IntegerNum(2)), ListExpr(VarExpr("a"), IntegerNum(1)), ListExpr(IntegerNum(2), IntegerNum(1)))
        val actual = Tally(ListExpr(IntegerNum(5), VarExpr("a"), IntegerNum(5), IntegerNum(2))).eval()
        assertEquals(expected, actual)
    }
}