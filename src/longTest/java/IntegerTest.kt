package org.cerion.symcalc

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