package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Rational
import kotlin.test.Test

internal class ModTest {

    @Test
    fun basic() {
        // NumberExpr class handles most everything here
        Mod(27, 6).eval() `should equal` 3
        Mod(-25, 6).eval() `should equal` 5

        Mod(25, 6.45).eval() `should equal` 5.649999999999999
        Mod(25, "6.45").eval() `should equal` "5.650"
    }

    @Test
    fun pi() {
        Mod(25, Pi()).eval() `should equal` Plus(25, Times(-7, Pi()))
        Mod(25, Times(2, Pi())).eval() `should equal` Plus(25, Times(-6, Pi()))
        Mod(-25, Pi()).eval() `should equal` Plus(-25, Times(8, Pi()))
        Mod(-25, Times(3, Pi())).eval() `should equal` Plus(-25, Times(9, Pi()))
        Mod(Rational(25, 2), Pi()).eval() `should equal` Plus(Rational(25, 2), Times(-3, Pi()))
    }

    @Test
    fun pi_precision() {
        Mod(25.0, Pi()).eval() `should equal` 3.008851424871448
        Mod("25.0", Pi()).eval() `should equal` "3.01"
        Mod("25.0", Times(2, Pi())).eval() `should equal` "6.15"
        Mod("6.4380", Times(2, Pi())).eval() `should equal` "0.15481"
    }
}