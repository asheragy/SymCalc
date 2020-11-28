package expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.list.Reverse
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class ReverseTest {

    @Test
    fun basic() {
        assertEquals(ListExpr(), Reverse(ListExpr()).eval())
        assertEquals(ListExpr(3, 2, 1), Reverse(ListExpr(1,2,3)).eval())
    }
}