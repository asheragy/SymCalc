package org.cerion.symcalc.expression

import expression.constant.I
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals


class Identities {

    @Test
    fun test() {
        assertAll(
                { assertEquals(Integer(42), Plus(Power(Integer("-80538738812075974"), Integer(3)), Power(Integer("80435758145817515"), Integer(3)), Power(Integer("12602123297335631"), Integer(3))).eval()) },
                { assertEquals(Integer(-1), Power(E(), Times(Pi(), I())).eval()) }
        )
    }

    // TODO trig identities
}