package expression.number

import org.cerion.symcalc.expression.number.*
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class RealNum_DoubleTest {

    @Test
    fun compare() {
        assertEquals(0, RealNum.create(5.000000).compareTo(IntegerNum(5)))
        assertEquals(-1, RealNum.create(4.999999999).compareTo(IntegerNum(5)))
        assertEquals(1, RealNum.create(5.000000000001).compareTo(IntegerNum(5)))

        assertEquals(0, RealNum.create(0.3333333333333333).compareTo(RationalNum(1,3)))
        assertEquals(-1, RealNum.create(0.3333333333333332).compareTo(RationalNum(1,3)))
        assertEquals(1, RealNum.create(0.3333333333333334).compareTo(RationalNum(1,3)))

        assertEquals(0, RealNum.create(5.12345).compareTo(RealNum.create(5.12345)))
        assertEquals(-1, RealNum.create(5.12344).compareTo(RealNum.create(5.12345)))
        assertEquals(1, RealNum.create(5.12346).compareTo(RealNum.create(5.12345)))

        assertEquals(0, RealNum.create(0.0001).compareTo(ComplexNum(RealNum.create((0.0001)), IntegerNumTest.zero)))
        assertFailsWith<UnsupportedOperationException> { RealNum.create(5.0).compareTo(ComplexNum(IntegerNumTest.zero, IntegerNumTest.one)) }
    }

    @Test
    fun addition() {
        assertEquals(RealNum.create(10.0), RealNum.create(5.0) + IntegerNum(5))
        assertEquals(RealNum.create(5.5), RealNum.create(5.0) + RationalNum(1,2))
        assertEquals(RealNum.create(9.9), RealNum.create(5.0) + RealNum.create(4.9))
        assertEquals(RealNum.create(15.0), RealNum.create(5.0) + RealNum.create(BigDecimal.TEN))
        assertEquals(ComplexNum(RealNum.create(10.0), IntegerNum(4)), RealNum.create(5.0) + ComplexNum(5,4))
    }

    @Test
    fun subtract() {
        assertEquals(RealNum.create(1.0), RealNum.create(5.0) - IntegerNum(4))
        assertEquals(RealNum.create(4.5), RealNum.create(5.0) - RationalNum(1,2))
        assertEquals(RealNum.create(0.5), RealNum.create(5.0) - RealNum.create(4.5))
        assertEquals(RealNum.create(-5.0), RealNum.create(5.0) - RealNum.create(BigDecimal.TEN))
        assertEquals(ComplexNum(RealNum.create(-5.0), IntegerNum(4)), RealNum.create(5.0) - ComplexNum(10,-4))
    }

    @Test
    fun times() {
        assertEquals(RealNum.create(20.0), RealNum.create(5.0) * IntegerNum(4))
        assertEquals(RealNum.create(2.5), RealNum.create(5.0) * RationalNum(1,2))
        assertEquals(RealNum.create(22.5), RealNum.create(5.0) * RealNum.create(4.5))
        assertEquals(RealNum.create(50.0), RealNum.create(5.0) * RealNum.create(BigDecimal.TEN))
        assertEquals(ComplexNum(RealNum.create(50.0), RealNum.create(-20.0)), RealNum.create(5.0) * ComplexNum(10,-4))
    }

    @Test
    fun divide() {
        assertEquals(RealNum.create(1.25), RealNum.create(5.0) / IntegerNum(4))
        assertEquals(RealNum.create(10.0), RealNum.create(5.0) / RationalNum(1,2))
        assertEquals(RealNum.create(2.0), RealNum.create(5.0) / RealNum.create(2.5))
        assertEquals(RealNum.create(0.5), RealNum.create(5.0) / RealNum.create(BigDecimal.TEN))
        assertEquals(ComplexNum(RealNum.create(5.0), RealNum.create(-10.0)), RealNum.create(50.0) / ComplexNum(2,4))
    }
}