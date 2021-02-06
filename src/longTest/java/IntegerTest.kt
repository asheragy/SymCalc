package org.cerion.symcalc

import org.cerion.symcalc.function.integer.Bernoulli
import org.cerion.symcalc.function.integer.Binomial
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.primitive.BigInt
import org.cerion.symcalc.number.primitive.toBigInt
import org.junit.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class IntegerTest {

    @Test
    fun simpleTest() {
        val a = BigInt("4294967295").add(BigInt("1")) // Result is about 1000 int32 digits
        val b = BigInt("79228162514264337593543950335").add(BigInt("1")) // Carry across multiple digits

        println(a.toString())
        println(b.toString())
    }

    @Test
    fun construct() {
        var x = 0
        var s = "123456789098764321".repeat(10000)
        repeat(10) {
            x += BigInt(s).signum()
        }
    }

    @Test
    fun constructJava() {
        var x = 0
        var s = "123456789098764321".repeat(10000)
        repeat(10) {
            x += BigInteger(s).signum()
        }
    }

    @Test
    fun test() {
        for(i in 1 until 5000) {
            val s = "1" + "0".repeat(i)
            val i = BigInt(s)
            //println(i).toString()
            assertEquals(s, i.toString())
        }
    }

    @Test
    fun string() {
        var x = 0
        val s = BigInt("123456789098764321".repeat(10000))
        repeat(1) {
            x += s.toString().length
        }
    }

    @Test
    fun stringJava() {
        var x = 0
        val s = BigInteger("123456789098764321".repeat(2))
        repeat(10000000) {
            x += s.toString().length
        }
    }

    @Test
    fun addition() {
        val a = BigInt("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        var x = BigInt("0")

        repeat(9000000) {
            x = x.add(a)
        }

        val str = x.toString()
        assertEquals('1', str.first())
        assertEquals('0', str.last())
    }

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
    fun additionBigSmall() {
        var a = BigInt("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        val x = BigInt("1234567890")
        repeat(10000000) {
            a += x
        }
    }

    @Test
    fun additionBigSmallJava() {
        var a = BigInteger("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        val x = BigInteger("1234567890")
        repeat(10000000) {
            a += x
        }
    }

    @Test
    fun additionSmallSmall() {
        val a = BigInt("1")
        var x = BigInt("0")
        repeat(100000000) {
            x += a
        }
    }

    @Test
    fun additionSmallSmallJava() {
        val a = BigInteger("1")
        var x = BigInteger("0")
        repeat(100000000) {
            x += a
        }
    }

    @Test
    fun subtraction() {
        val a1 = BigInteger("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        val subtract1 = a1 / BigInteger("293840932483209")

        var a = a1.toBigInt()
        val subtract = subtract1.toBigInt()

        repeat(10000000) {
            a -= subtract
        }

        val str = a.toString()
        assertEquals('1', str.first())
        assertEquals('1', str.last())
    }

    @Test
    fun subtractionJava() {
        var a = BigInteger("123456789098764321".repeat(550)) // Result is about 1000 int32 digits
        val subtract = a / BigInteger("293840932483209")

        repeat(10000000) {
            a -= subtract
        }

        val str = a.toString()
        assertEquals('1', str.first())
        assertEquals('1', str.last())
    }

    @Test
    fun multiply() {
        val a = BigInt("123456789098764321".repeat(40))
        val b = a + BigInt.ONE
        repeat(60000) {
            a * b
        }
    }

    @Test
    fun multiplyJava() {
        val a = BigInteger("123456789098764321".repeat(40))
        val b = a + BigInteger.ONE
        repeat(600000) {
            a * b
        }
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

    @Test
    // ~1.5s
    fun binomial() {
        val a = Binomial(500)
        repeat(500) {
            a.eval()
        }
    }

    @Test
    // ~1.2
    fun bernoulli() {
        val a = Bernoulli(100)
        repeat(200) {
            a.eval()
        }
    }
}