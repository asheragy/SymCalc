package expression.function.integer

import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ModTest {

    @Test
    fun basic() {
        assertEquals(Integer(3), Mod(Integer(27), Integer(6)).eval())
        assertEquals(Integer(5), Mod(Integer(-25), Integer(6)).eval())
    }

    @Test
    fun real() {
        assertEquals(RealDouble(5.6499999999999995), Mod(Integer(25), RealDouble(6.45)).eval())
        assertEquals(RealDouble(0.8000000000000007), Mod(Integer(-25), RealDouble(6.45)).eval())

        assertEquals(RealBigDec("5.650"), Mod(Integer(25), RealBigDec("6.45")).eval())
        assertEquals(RealBigDec("0.80"), Mod(Integer(-25), RealBigDec("6.45")).eval())
    }
}