package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class BoolExprTest {

    @Test
    fun equals() {
        val e1 = BoolExpr.TRUE
        val e2 = BoolExpr.FALSE

        // Different
        assertNotEquals(e1, e2)
        assertNotEquals(e2, e1)

        // Same
        assertEquals(e1, e1)
        assertEquals(e2, e2)

        // Different object type
        assertNotEquals(e1 as Expr, Integer(5))
        assertNotEquals(e1 as Expr, ListExpr())
    }

}