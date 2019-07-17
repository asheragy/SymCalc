package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.junit.Test

import org.junit.Assert.*


class SqrtTest {

    @Test
    fun validate() {
        assertEquals(Expr.ExprType.ERROR, Sqrt().eval().type)
        assertEquals(Expr.ExprType.ERROR, Sqrt(IntegerNum(1), IntegerNum(2)).eval().type)
    }

    @Test
    fun basic() {
        // Converts to power
        assertEquals(Power(IntegerNum(3), RationalNum(1, 2)), Sqrt(IntegerNum(3)).eval())

        // Evaluates when square number
        assertEquals(IntegerNum(3), Sqrt(IntegerNum(9)).eval())
        assertEquals(IntegerNum(4), Sqrt(IntegerNum(16)).eval())
        assertEquals(IntegerNum("985236412054"), Sqrt(IntegerNum("970690787637039276498916")).eval())
    }

    @Test
    fun reduces() {
        // 8 = 2*sqrt(2)
        assertEquals(Times(IntegerNum(2), Power(IntegerNum(2), RationalNum(1, 2))),
                Sqrt(IntegerNum(8)).eval())

        // 497664 = 288 * sqrt(6)
        assertEquals(Times(IntegerNum(288), Power(IntegerNum(6), RationalNum(1, 2))),
                Sqrt(IntegerNum(497664)).eval())
    }
}