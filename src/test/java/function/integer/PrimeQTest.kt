package org.cerion.symcalc.function.integer


import org.cerion.symcalc.`==`
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.function.arithmetic.Power
import kotlin.test.Test

internal class PrimeQTest {

    @Test
    fun primes() {
        val primes = listOf(2,3,5,541,7919,104729,1299709,15485863,2038074743,252097800623,29996224275833)

        for(n in primes)
            PrimeQ(n).eval() `should equal` BoolExpr.TRUE
    }

    @Test
    fun nonPrimes() {
        val composites = listOf(1,4,2047,1373653,25326001,3215031751,1689259081189,
                2152302898747,3474749660383,341550071728321, "63553301080466445991867")

        for(n in composites)
            PrimeQ(n).eval() `should equal` BoolExpr.FALSE
    }

    @Test
    fun mersennePrimes() {
        assertAll(
                PrimeQ(Power(2, 31) - 1) `==` BoolExpr.TRUE,
                //PrimeQ(Power(2, 61) - 1) `==` BoolExpr.TRUE,
                //PrimeQ(Power(2, 107) - 1) `==` BoolExpr.TRUE,
                //PrimeQ(Power(2, 521) - 1) `==` BoolExpr.TRUE,
                //PrimeQ(Power(2, 1279) - 1) `==` BoolExpr.TRUE
        )
    }
}