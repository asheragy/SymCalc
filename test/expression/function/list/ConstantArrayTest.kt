package expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.*
import org.junit.Test

class ConstantArrayTest {

    @Test
    fun basic() {
        val a = IntegerNum(55)
        val array = ConstantArray(a, IntegerNum(5))

        assertEquals(ListExpr(a,a,a,a,a), array.eval())
    }

    @Test
    fun expr() {
        val a = Plus(IntegerNum(5), VarExpr("x"))
        val array = ConstantArray(a, IntegerNum(5))

        assertEquals(ListExpr(a,a,a,a,a), array.eval())
    }

    @Test
    fun evaluates_subExprs() {
        val a = Plus(IntegerNum(2), IntegerNum(2))
        val b = IntegerNum(4)
        val array = ConstantArray(a, IntegerNum(5))

        assertEquals(ListExpr(b,b,b,b,b), array.eval())
    }
}