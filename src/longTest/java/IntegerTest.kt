package org.cerion.symcalc

import org.cerion.symcalc.number.Integer
import org.junit.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class IntegerTest {

    @Test
    fun additionJava() {
        val a = BigInteger("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        var x = BigInteger.ZERO

        repeat(9000000) {
            x += a
        }

        val str = x.toString()
        assertEquals('1', str.first())
        assertEquals('0', str.last())
    }

    @Test
    fun basicOps() {
        val a = Integer("9".repeat(50000))
        val b = Integer("7".repeat(50000))
        val c = Integer("3".repeat(99999))
        val d = Integer("1".repeat(99980))

        repeat(200) {
            var x = a + a
            x *= b
            x -= c
            val t = x.value.divide(d.value)

            assertEquals("1369999999999999999999", t.toString())
        }
    }
}