package expression.number

import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_BigDecimal
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class RealNum_BigDecimalTest {

    @Test
    fun precision() {
        assertEquals(11, RealNum_BigDecimal("1.0000000001").precision)
        assertEquals(1, RealNum_BigDecimal("0.0000000001").precision)
        assertEquals(2, RealNum_BigDecimal("0.0000000011").precision)
        assertEquals(13, RealNum_BigDecimal("111.0000000011").precision)
    }

    @Test
    fun precision_evaluate() {
        assertEquals(RealNum_BigDecimal("111.000000001"), RealNum_BigDecimal("111.0000000011").evaluate(12))
    }

    @Test
    fun multiply() {
        assertEquals(RealNum_BigDecimal("0.0002468"), RealNum_BigDecimal("0.0001234") * IntegerNum.TWO)
        assertEquals(RealNum_BigDecimal("0.0001851"), RealNum_BigDecimal("0.0001234") * RationalNum(3,2))
        assertEquals(RealNum.create(0.0037427219999999995), RealNum_BigDecimal("0.0001234") * RealNum.create(30.33))
        assertEquals(RealNum_BigDecimal("0.003743"), RealNum_BigDecimal("0.0001234") * RealNum_BigDecimal("30.33"))
    }
}