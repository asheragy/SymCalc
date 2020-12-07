package org.cerion.symcalc.function.special

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll

internal class ZetaTest {

    @Test
    fun realBigDec() {
        assertAll(
                // TODO this is only accepting int values and returning bigDec
                Zeta(RealBigDec("2.000000000")) `==` RealBigDec("1.644934067")
        )
    }
}