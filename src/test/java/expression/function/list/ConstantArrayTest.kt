package expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConstantArrayTest {

    @Test
    fun basic() {
        val a = Integer(55)
        val array = ConstantArray(a, Integer(5))

        assertEquals(ListExpr(a,a,a,a,a), array.eval())
    }

    @Test
    fun expr() {
        val a = Plus(Integer(5), VarExpr("x"))
        val array = ConstantArray(a, Integer(5))

        assertEquals(ListExpr(a,a,a,a,a), array.eval())
    }

    @Test
    fun evaluates_subExprs() {
        val a = Plus(Integer(2), Integer(2))
        val b = Integer(4)
        val array = ConstantArray(a, Integer(5))

        assertEquals(ListExpr(b,b,b,b,b), array.eval())
    }
}