package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Integer
import org.junit.Test

class TrigTest {

    // TODO add versions of this for hyperbolic

    // TODO add more inputs here like 0 and Pi

    @Test
    fun complexInfinity() {
        val ci = ComplexInfinity()
        Sin(ci) `==` Indeterminate()
        Cos(ci) `==` Indeterminate()
        Tan(ci) `==` Indeterminate()
        Sec(ci) `==` Indeterminate()
        Csc(ci) `==` Indeterminate()
        Cot(ci) `==` Indeterminate()

        ArcSin(ci) `==` ComplexInfinity()
        ArcCos(ci) `==` ComplexInfinity()
        ArcTan(ci) `==` Indeterminate()
        ArcSec(ci) `==` Pi() / Integer.TWO
        ArcCsc(ci) `==` 0
        ArcCot(ci) `==` 0
    }
}