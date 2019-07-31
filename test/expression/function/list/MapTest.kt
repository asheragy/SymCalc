package expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.list.Map
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.junit.Assert.*
import org.junit.Test

class MapTest {
    @Test
    fun basic() {
        val list = ListExpr(IntegerNum.ZERO, Times(Pi(), RationalNum(1,2)), Pi(), Times(Pi(), RationalNum(3,2)))
        assertEquals(ListExpr(0, 1, 0, -1), Map(Sin().symbol, list).eval())
    }
}