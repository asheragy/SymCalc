package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class GammaTest {

    @Test
    fun realBigDec() {
        assertAll(
                Gamma(RealBigDec("0.1111111111")) `==` "8.522688140",
                Gamma(RealBigDec("1.222222222")) `==` "0.9125732371",
                Gamma(RealBigDec("2.222222222")) `==` "1.115367290",
                Gamma(RealBigDec("6.54321")) `==` "311.123",
                Gamma(RealBigDec("-3.222222222")) `==` "0.6119578328"
        )
    }
}