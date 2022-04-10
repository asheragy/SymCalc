package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import kotlin.test.Ignore
import kotlin.test.assertEquals

internal class GammaTest {

    @Test
    fun integers() {
        assertAll(
                Gamma(-100) `==` ComplexInfinity(),
                Gamma(-99) `==` ComplexInfinity(),
                Gamma(-2) `==` ComplexInfinity(),
                Gamma(-1) `==` ComplexInfinity(),
                Gamma(0) `==` ComplexInfinity(),
                Gamma(1) `==` 1,
                Gamma(2) `==` 1,
                Gamma(10) `==` 362880)
    }

    @Test
    fun rational() {
        assertAll(
                Gamma(Rational(-21, 2)) `==` Times(Rational(-2048, "13749310575"), Power(Pi(), Rational.HALF)),
                Gamma(Rational(-3, 2)) `==` Times(Rational(4, 3), Power(Pi(), Rational.HALF)),
                Gamma(Rational(-1, 2)) `==` Times(-2, Power(Pi(), Rational.HALF)),
                Gamma(Rational(1, 2)) `==` Power(Pi(), Rational.HALF),
                Gamma(Rational(3, 2)) `==` Times(Rational.HALF, Power(Pi(), Rational.HALF)),
                Gamma(Rational(21, 2)) `==` Times(Rational(654729075, 1024), Power(Pi(), Rational.HALF)),
                // Not computable
                Gamma(Rational(1, 3)) `==` Gamma(Rational(1, 3)),
                Gamma(Rational(-3, 5)) `==` Gamma(Rational(-3, 5)))
    }

    @Test
    fun realBigDec() {
        assertAll(
                Gamma(RealBigDec("0.1111111111")) `==` "8.522688140",
                Gamma(RealBigDec("1.222222222")) `==` "0.9125732371",
                Gamma(RealBigDec("2.222222222")) `==` "1.115367290",
                Gamma(RealBigDec("5.555555555")) `==` "57.26133666",
                Gamma(RealBigDec("6.54321")) `==` "311.123",
                Gamma(RealBigDec("-3.222222222")) `==` "0.6119578328"
        )
    }

    @Test
    fun realBigDec_highPrecision() {
        val precision = 20 // Works up to 100
        assertAll(
                Gamma(RealBigDec("0.001", precision)) `==` RealBigDec("999.4237724845954661149822012996440004652176101456122324695421716913960238118284038452483877721898401", precision),
                Gamma(RealBigDec("0.999", precision)) `==` RealBigDec("1.000578205629358647990097615322270747269724531183709711048239559333598723240011856171670771215582027", precision),
                Gamma(RealBigDec("1.500", precision)) `==` RealBigDec("0.8862269254527580136490837416705725913987747280611935641069038949264556422955160906874753283692723327", precision),
                Gamma(RealBigDec("1.999", precision)) `==` RealBigDec("0.9995776274237292893421075177069484765224548066525260013371913197742651245167718443154991004443664454", precision),
                Gamma(Pi().eval(precision))           `==` RealBigDec("2.2880377953400324179595889090602339228896881533562224411993807454704710066085042825007253044679284748", precision),
                Gamma(RealBigDec("5.555", precision)) `==` RealBigDec("57.20975946055917813812606162394679559071496013293777144148922868004267627177579107765898895127159773", precision),
                Gamma(RealBigDec("100.1", precision)) `==` RealBigDec("1478454494651513679874739643700584598156163305312630712228634114648770721338127073225051892034559658000000000000000000000000000000000000000000000000000000000", precision)
        )
    }

    // ----------------- Performance tests that will eventually go in bignum when this is implemented there
    @Test
    @Ignore
    // ~1000ms
    fun gammaLarge() {
        val x = RealBigDec("3.14", 430)
        run(Gamma(x))
    }

    // ~1000ms
    @Test
    @Ignore
    fun gammaMultiple() {
        val x = RealBigDec("3.14", 100)
        run(90, Gamma(x))
    }

    // ~700ms
    @Test
    @Ignore
    fun `gamma precision step`() {
        val expected = RealBigDec("0.88622692545275801364908374167057259139877472806119356410690389492645564229551609068747532836927233270811341181214128533311807643286221130126254685480139353423101884932655256142496258651447541311446604768963398140008731950767573986025835009509261700929272348724745632015696088776295310820270966625045319920380686673873757671683399489468292591820439772558258086938002953369671589566640492742312409245102732742609780662578082373375752136938052805399806355360503018602224183618264830685404716174941583421")
        for(i in 1..100) {
            assertEquals(RealBigDec(expected.value, i), Gamma(RealBigDec("1.5", i)).eval())
        }
    }

    private fun run(times: Int, expr: Expr) {
        repeat(times) {
            run(expr)
        }
    }

    private fun run(expr: Expr) {
        val res = expr.eval()
        if (res is ErrorExpr)
            Assert.fail(res.toString())
    }
}