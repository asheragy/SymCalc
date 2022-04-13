package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.junit.Test

internal class CoshTest {

    @Test
    fun exact() {
        assertAll(
                Cosh(Integer.ZERO) `==` Integer.ONE,
                Cosh(Integer.TWO) `==` Cosh(Integer.TWO),
                Cosh(Infinity()) `==` Infinity(),
                Cosh(ComplexInfinity()) `==` Indeterminate(),
                Cosh(Log(Integer(2))) `==` Rational(5, 4),
                Cosh(Pi() * Complex(0, Rational.HALF)) `==` Integer.ZERO,
                //Cosh(Pi() * Complex(0, Rational(1,4))) `==` Power(Integer.TWO, Rational(-1,2))
                //Cosh(Pi() * Complex(0, Rational(1,5))) `==`
                //Cosh(Log(golden ratio)) == 1/2 * Sqrt(5)
        )
    }

    @Test
    fun bigDecimal() {
        assertAll(
                Cosh(RealBigDec("0.00")) `==` RealBigDec("1.0"),
                Cosh(RealBigDec("1.000000000")) `==` RealBigDec("1.543080635"),
                Cosh(RealBigDec("2.1555")) `==` RealBigDec("4.3740"),
                Cosh(RealBigDec("3.1415")) `==` RealBigDec("11.591"),
                Cosh(RealBigDec("3.1415926535897932384626433832795028841971693993751")) `==` RealBigDec("11.591953275521520627751752052560137695770917176205"),
                //Cosh(RealBigDec("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491"))
                //        `==` RealBigDec("11.591953275521520627751752052560137695770917176205422538212883048462696558223735375607555978514725152031484755882842717600377282123236747254491452413329307393535509626533035624039919478217911714001790246571399472096790683878384759297363507132201658216252723798253959006604064456441714961342633347164941526560554133278083572489942271655602699898004214902475818749724711585235935764914349561059596266689625496559019198157380382352929679903947283233559862669537439061093801190431773390371314112948213422"),

                Cosh(RealBigDec("5.4321")) `==` RealBigDec("114.32"),
                Cosh(RealBigDec("-5.4321")) `==` RealBigDec("114.32"),
                Cosh(RealBigDec("9.999999999")) `==` RealBigDec("11013.23291"),
        )
    }
}