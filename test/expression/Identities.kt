package org.cerion.symcalc.expression

import expression.constant.I
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.trig.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll


class Identities {

    @Test
    fun power() {
        assertAll(
                { Power("-80538738812075974", 3) + Power("80435758145817515", 3) + Power("12602123297335631", 3) `should equal` 42 },
                { Power(E(), Pi() * I()).eval() `should equal` -1 }
        )
    }

    @Test
    fun trigIdentities() {
        val x = VarExpr("x")

        // TODO_LP get these working
        assertAll(
                //{ Divide(Sin(x), Cos(x)).eval()  `should equal` Tan(x) }
        )
    }
}