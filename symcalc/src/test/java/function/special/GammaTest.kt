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

    // For testing any major algorithm changes
    @Ignore
    @Test
    fun realBigDec_halfValues() {
        for(precision in 1..100) {
            assertAll(
                Gamma(RealBigDec("-30.5", precision)) `==` RealBigDec("-0.000000000000000000000000000000002135797443694174559898291676231031631343039677892576847149112668736526563729267097770655622629024724", precision),
                Gamma(RealBigDec("-10.5", precision)) `==` RealBigDec("-0.0000002640121820547716316246385325311240439682468432522587656059168154777653141232089673307782521306919765", precision),
                Gamma(RealBigDec("-2.5", precision)) `==` RealBigDec("-0.9453087204829418812256893244486107641586930432652731350473641545882193517818838300666403502605571549", precision),
                Gamma(RealBigDec("-1.5", precision)) `==` RealBigDec("2.363271801207354703064223311121526910396732608163182837618410386470548379454709575166600875651392887", precision),
                Gamma(RealBigDec("-0.5", precision)) `==` RealBigDec("-3.544907701811032054596334966682290365595098912244774256427615579705822569182064362749901313477089331", precision),
                Gamma(RealBigDec("0.5", precision)) `==` RealBigDec("1.772453850905516027298167483341145182797549456122387128213807789852911284591032181374950656738544665", precision),
                Gamma(RealBigDec("1.5", precision)) `==` RealBigDec("0.8862269254527580136490837416705725913987747280611935641069038949264556422955160906874753283692723327", precision),
                Gamma(RealBigDec("2.5", precision)) `==` RealBigDec("1.329340388179137020473625612505858887098162092091790346160355842389683463443274136031212992553908499", precision),
                Gamma(RealBigDec("4.5", precision)) `==` RealBigDec("11.63172839656744892914422410942626526210891830580316552890311362090973030512864869027311368484669937", precision),
                Gamma(RealBigDec("30.5", precision)) `==` RealBigDec("48226969334909086010917483030261.2545074465525956899632826414173535278590206304203076369900445213641", precision)
            )
        }
    }

    // For testing algorithm changes on large number
    @Test
    @Ignore
    fun realBigDec_maxPrecision() {
        val expected = "1.329340388179137020473625612505858887098162092091790346160355842389683463443274136031212992553908499062170117718211927999677114649293316951893820282202090301346528273989828842137443879771713119671699071534450972100130979261513609790387525142638925513939085230871184480235441331644429662304064499375679798805710300108106365075250992342024388877306596588373871304070044300545073843499607391134686138676540991139146709938671235600636282054070792080997095330407545279033362754273972460281070742624123751318160384282206840315320839333551521074927503913729263844060047477708490636133037520412491457197017796701648918315406612461037440567364799156128879447980163660521964358424798734208902715861809221715542052434483648884851153449884379719945033492286582256270109372697946565346170571464163043643802655844568574290749675496667300710852245936141557302484126403796098332890638980980180692884389226969812469964197886931652916827974804835470479631008782251605423175074779446317100632252240012778240622607240502"
        for(precision in 100..500 step 100) {
            assertEquals(RealBigDec(expected, precision), Gamma(RealBigDec("2.5", precision)).eval())
        }
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