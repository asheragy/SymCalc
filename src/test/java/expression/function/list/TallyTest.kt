package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TallyTest {

    @Test
    fun basic() {
        assertEquals(ListExpr(ListExpr(VarExpr("a"), Integer(3))),
                Tally(ListExpr(VarExpr("a"), VarExpr("a"), VarExpr("a"))).eval())

        val expected = ListExpr(ListExpr(Integer(5), Integer(2)), ListExpr(VarExpr("a"), Integer(1)), ListExpr(Integer(2), Integer(1)))
        val actual = Tally(ListExpr(Integer(5), VarExpr("a"), Integer(5), Integer(2))).eval()
        assertEquals(expected, actual)
    }
}