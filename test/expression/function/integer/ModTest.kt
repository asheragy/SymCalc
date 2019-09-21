package expression.function.integer

import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
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

    @Test
    fun pi() {
        assertEquals(Plus(Integer(25), Times(Integer(-7), Pi())), Mod(Integer(25), Pi()).eval())
        assertEquals(Plus(Integer(25), Times(Integer(-6), Pi())), Mod(Integer(25), Times(Integer(2), Pi())).eval())

        assertEquals(Plus(Integer(-25), Times(Integer(8), Pi())), Mod(Integer(-25), Pi()).eval())
        assertEquals(Plus(Integer(-25), Times(Integer(9), Pi())), Mod(Integer(-25), Times(Integer(3), Pi())).eval())

        assertEquals(Plus(Rational(25, 2), Times(Integer(-3), Pi())), Mod(Rational(25, 2), Pi()).eval())
    }

    @Test
    fun pi_precision() {
        assertEquals(RealDouble(3.008851424871448), Mod(RealDouble(25.0), Pi()).eval())
        assertEquals(RealBigDec("3.01"), Mod(RealBigDec("25.0"), Pi()).eval())
        assertEquals(RealBigDec("6.16"), Mod(RealBigDec("25.0"), Times(Integer(2), Pi())).eval())
        // TODO this should be 0.15481
        assertEquals(RealBigDec("0.1548"), Mod(RealBigDec("6.4380"), Times(Integer(2), Pi())).eval())
    }
}