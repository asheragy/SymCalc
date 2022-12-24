package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Rational
import org.junit.Test

class ArcTest {

    @Test
    fun arcSec_exact() {
        ArcSec(-2) `==` Rational(2,3) * Pi()
        ArcSec(-1) `==` Pi()
        ArcSec(1) `==` 0
        ArcSec(2) `==` Rational(1,3) * Pi()
    }

    @Test
    fun arcSec() {
        ArcSec(-3.0) `==` 1.9106332362490186
        ArcSec(-2.0) `==` 2.0943951023931957
        ArcSec(-1.0) `==` 3.141592653589793
        ArcSec(1.0) `==` 0.0
        ArcSec(2.0) `==` 1.0471975511965979
        ArcSec(3.0) `==` 1.2309594173407747
    }

    @Test
    fun arcCsc_exact() {
        ArcCsc(-2) `==` Rational(-1,6) * Pi()
        ArcCsc(-1) `==` Rational(-1, 2) * Pi()
        ArcCsc(1) `==` Rational(1, 2) * Pi()
        ArcCsc(2) `==` Rational(1,6) * Pi()
    }

    @Test
    fun arcCsc() {
        ArcCsc(-3.0) `==` -0.3398369094541219
        ArcCsc(-2.0) `==` -0.5235987755982989
        ArcCsc(-1.0) `==` -1.5707963267948966
        ArcCsc(1.0) `==` 1.5707963267948966
    }

    @Test
    fun arcCot_exact() {
        ArcCot(-1) `==` Rational(-1,4) * Pi()
        ArcCot(1) `==` Rational(1,4) * Pi()
    }

    @Test
    fun arcCot() {
        ArcCot(-2.0) `==` -0.4636476090008061
        ArcCot(-1.0) `==` -0.7853981633974483
        ArcCot(0.0) `==` 1.5707963267948966
        ArcCot(1.0) `==` 0.7853981633974483
        ArcCot(2.0) `==` 0.4636476090008061
    }
}