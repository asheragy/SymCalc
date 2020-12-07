package org.cerion.symcalc.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RandomChoiceTest {

    @Test
    fun basic() {
        val a = VarExpr("a")
        val b = VarExpr("b")
        val c = Integer(6)

        val e = RandomChoice(ListExpr(a, b, c))
        val found = booleanArrayOf(false, false, false)
        for (i in 0..99) {
            val eval = e.eval()

            if (eval.equals(a))
                found[0] = true
            else if (eval.equals(b))
                found[1] = true
            else if (eval.equals(c))
                found[2] = true

            if (found[0] && found[1] && found[2])
                return
        }

        assertFalse(true)
    }

    @Test
    fun basicList() {
        val a = VarExpr("a")
        val b = VarExpr("b")
        val c = Integer(6)
        var e: Expr = RandomChoice(ListExpr(a, b, c), Integer(100))
        e = e.eval()

        assertTrue(e.isList)
        val list = e as ListExpr
        assertEquals(100, list.size.toLong())

        val found = booleanArrayOf(false, false, false)
        for (i in 0..99) {
            val curr = list[i]

            if (curr.equals(a))
                found[0] = true
            else if (curr.equals(b))
                found[1] = true
            else if (curr.equals(c))
                found[2] = true
        }

        assertTrue(found[0] && found[1] && found[2])
    }
}