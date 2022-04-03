package org.cerion.symcalc

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Zeta
import org.cerion.symcalc.number.Integer
import org.junit.Test
import kotlin.test.assertEquals

private const val MIN_PRECISION = 100

class Special {

    @Test
    fun gammaLarge() {
        val x = RealBigDec("3.14", 400)
        run(Gamma(x))
    }

    @Test
    fun gammaMultiple() {
        val x = RealBigDec("3.14", MIN_PRECISION)
        run(80, Gamma(x))
    }

    @Test
    fun `gamma precision step`() {
        val expected = RealBigDec("0.88622692545275801364908374167057259139877472806119356410690389492645564229551609068747532836927233270811341181214128533311807643286221130126254685480139353423101884932655256142496258651447541311446604768963398140008731950767573986025835009509261700929272348724745632015696088776295310820270966625045319920380686673873757671683399489468292591820439772558258086938002953369671589566640492742312409245102732742609780662578082373375752136938052805399806355360503018602224183618264830685404716174941583421")
        for(i in 1..100) {
            assertEquals(RealBigDec(expected.value, i), Gamma(RealBigDec("1.5", i)).eval())
        }
    }

    @Test
    fun zetaLarge() {
        val x = RealBigDec("3.14", 530)
        run(Zeta(x))
    }

    @Test
    fun zetaMultiple() {
        val x = RealBigDec("3.14", 70)
        for(i in 0 until 90)
            if(Zeta(x).eval() is ErrorExpr) // TODO add wrapper for everything that checks result
                throw RuntimeException()
    }

    @Test
    fun zetaOddInteger_multiple() {
        repeat(7) {
            for (i in 3 until 100) {
                run(Zeta(Integer(i).toPrecision(100)))
            }
        }
    }

    @Test
    fun zetaOddInteger_Large() {
        for (i in 3 until 100) {
            run(Zeta(Integer(i).toPrecision(450)))
        }
    }
}