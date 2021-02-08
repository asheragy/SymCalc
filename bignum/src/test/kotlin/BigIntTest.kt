package org.cerion.math.bignum

import org.junit.Test
import org.junit.jupiter.api.Assertions
import kotlin.test.*


@ExperimentalUnsignedTypes
internal class BigIntTest {

    private fun bigInt(vararg n: Int): BigInt = BigInt(1, n.map { it.toUInt() }.reversed().toUIntArray())

    @Test
    fun parse() {
        // Tests to and from string
        assertEquals("1", BigInt("1").toString())
        assertEquals("2000000000", BigInt("2000000000").toString())
        assertEquals("-2000000000", BigInt("-2000000000").toString())
        assertEquals("4000000000", BigInt("4000000000").toString())
        assertEquals("4294967295", BigInt("4294967295").toString())
        assertEquals("4294967296", BigInt("4294967296").toString())
        assertEquals("18446744078004518913", BigInt("18446744078004518913").toString())
        assertEquals("36893488147419103231", BigInt("36893488147419103231").toString())
        assertEquals("79228162514264337593543950335", BigInt("79228162514264337593543950335").toString())
        assertEquals("100000000000000000000000000000000", BigInt("100000000000000000000000000000000").toString())

        // toString requires addition carry
        assertEquals("10000000000000000000000000", BigInt("10000000000000000000000000").toString())

        // Multiple addition carries at the end
        assertEquals("340282366920938463463374607431768211456", BigInt("340282366920938463463374607431768211456").toString())
    }

    @Test
    fun addition() {
        assertEquals(bigInt(1,0), bigInt(-1) + bigInt(1))

        // Digit carried
        assertEquals(bigInt(1,0,0), bigInt(-1,-1) + bigInt(1))
        assertEquals(bigInt(1,2,0), bigInt(1,1,-1) + bigInt(1))

        // Larger second
        assertEquals(bigInt(1,1,1,2), bigInt(1) + bigInt(1,1,1,1))
    }

    @Test
    fun subtract() {
        assertEquals(bigInt(2,2), bigInt(3,3) - bigInt(1,1))

        // Digits borrowed
        assertEquals(bigInt(4,-1), bigInt(5,1) - bigInt(2))
        assertEquals(bigInt(3,-4), bigInt(5,1) - bigInt(1,5))
        assertEquals(bigInt(1,-2,-1), bigInt(2,0,1) - bigInt(1,2))
        assertEquals(bigInt(1,3,-4), bigInt(1,5,1) - bigInt(1,5))

        // Result shrinks in size
        assertEquals(bigInt(1), bigInt(1,1,1,2) - bigInt(1,1,1,1))

        // Negative
        assertEquals(bigInt(2,2).negate(), bigInt(1,1) - bigInt(3,3))
    }

    @Test
    fun additionSigned() {
        assertEquals(BigInt("10000000000"), BigInt("21111111111") + BigInt("-11111111111"))
        assertEquals(BigInt("-20000000000"), BigInt("21111111111") + BigInt("-41111111111"))

        assertEquals(BigInt("-10000000000"), BigInt("-21111111111") + BigInt("11111111111"))
        assertEquals(BigInt("30000000000"), BigInt("-21111111111") + BigInt("51111111111"))

        assertEquals(BigInt("-72222222222"), BigInt("-21111111111") + BigInt("-51111111111"))
    }

    @Test
    fun subtractionSigned() {
        // One negative (internal addition)
        assertEquals(BigInt("32222222222"), BigInt("21111111111") - BigInt("-11111111111"))
        assertEquals(BigInt("-32222222222"), BigInt("-21111111111") - BigInt("11111111111"))

        // Both negative
        assertEquals(BigInt("-10000000000"), BigInt("-21111111111") - BigInt("-11111111111"))
        assertEquals(BigInt("30000000000"), BigInt("-21111111111") - BigInt("-51111111111"))
    }

    @Test
    fun multiplySingleDigit() {
        assertEquals(bigInt(10,10,10), bigInt(1,1,1) * bigInt(10))
        assertEquals(bigInt(9,-1,-1,-10), bigInt(-1,-1,-1) * bigInt(10))
    }

    @Test
    fun multiply() {
        assertEquals(bigInt(1,2,3,2,1), bigInt(1,1,1) * bigInt(1,1,1))

        // Digits carried
        assertEquals(bigInt(-2,-1,-1,1), bigInt(-1,-1,-1) * bigInt(-1))
        assertEquals(bigInt(-1,-1,-2,0,0,1), bigInt(-1,-1,-1) * bigInt(-1,-1,-1))
    }

    @Test
    fun multiplySigned() {
        val zero = BigInt.ZERO
        val a = BigInt("100000000000")
        val abig = BigInt("10000000000000000000000")

        assertEquals(zero, zero * zero)
        assertEquals(zero, zero * a)
        assertEquals(zero, zero * a.negate())
        assertEquals(zero, a * zero)
        assertEquals(zero, a.negate() * zero)

        assertEquals(abig, a * a)
        assertEquals(abig.negate(), a.negate() * a)
        assertEquals(abig.negate(), a * a.negate())
        assertEquals(abig, a.negate() * a.negate())
    }

    @Test
    fun divisionSingleDigit() {
        assertEquals(bigInt(1,1,1,1), bigInt(1,0,0,0,0).divide(4294967295u))
        assertEquals(bigInt(1,1,1,1), bigInt(-1,-1,-1,-1).divide(4294967295u))
        assertEquals(bigInt(2147483647,-1,-1,-1), bigInt(-1,-1,-1,-2).divide(2u))
    }

    @Test
    fun division() {
        assertEquals(Pair(bigInt(-1), BigInt.ZERO), bigInt(-1,-1).divideAndRemainder(bigInt(1,1)))
        assertEquals(Pair(bigInt(-1), BigInt.ONE), bigInt(1, 0, 0).divideAndRemainder(bigInt(1,1)))
        assertEquals(Pair(bigInt(-1,0), BigInt.ZERO), bigInt(-1,-1,0).divideAndRemainder(bigInt(1,1)))
        assertEquals(Pair(bigInt(-1,0), bigInt(-1)), bigInt(-1,-1,-1).divideAndRemainder(bigInt(1,1)))
        assertEquals(Pair(bigInt(-1,0,-1), BigInt.ZERO), bigInt(-1,-1,-1,-1).divideAndRemainder(bigInt(1,1)))

        assertEquals(Pair(BigInt("5384072782"), BigInt("12548692603041114205")), BigInt("75628712337421087763136048659").divideAndRemainder(BigInt("14046747766433220397")))
        assertEquals(Pair(BigInt("68"), BigInt("999999999999")), BigInt("7556555555555547").divideAndRemainder(BigInt("111111111111111")))
        assertEquals(Pair(BigInt("9000000000000"), BigInt("999999999999")), BigInt("999999999999999999999999999").divideAndRemainder(BigInt("111111111111111")))

        // Negative remainder
        assertEquals(Pair(BigInt("-24"), BigInt("-1")), BigInt("-49").divideAndRemainder(BigInt(2)))
        assertEquals(Pair(BigInt("-12"), BigInt("-2")), BigInt(-350).divideAndRemainder(BigInt(29)))
    }

    @Test
    fun debug() {
        assertEquals(Pair(BigInt("-12"), BigInt("-2")), BigInt(-350).divideAndRemainder(BigInt(29)))
    }

    @Test
    fun divisionSigned() {
        assertEquals(BigInt(5), BigInt(25) / BigInt(5))
        assertEquals(BigInt(-5), BigInt(-25) / BigInt(5))
        assertEquals(BigInt(-5), BigInt(25) / BigInt(-5))
        assertEquals(BigInt(5), BigInt(-25) / BigInt(-5))
    }

    /*
    @Test
    fun divisionRandom() {

        val rand = Random()
        val a = BigInt(1, arrayOfNulls<Any>(3).map { rand.nextInt().toUInt() }.toUIntArray())
        val b = BigInt(1, arrayOfNulls<Any>(2).map { rand.nextInt().toUInt() }.toUIntArray())

        repeat(1000) {
            val c = BigInteger(a.toString())
            val d = BigInteger(b.toString())
            val expected = c.divide(d)

            println(expected)
            assertEquals(expected.toString(), (a / b).toString(), "$a / $b")
        }
    }
     */

    @Test
    fun compare() {
        assertTrue(bigInt(1,2) > bigInt(9))
        assertTrue(bigInt(9) < bigInt(1,0))

        // Negative is opposite
        assertTrue(bigInt(1,2).negate() < bigInt(9))
        assertTrue(bigInt(9) > bigInt(1,0).negate())

        // Equal
        assertEquals(bigInt(1,1), bigInt(1,1))

        // Not Equal
        assertNotEquals(bigInt(1,1), bigInt(1,1).negate())
        assertNotEquals(bigInt(1,1), bigInt(1,1,1))
        assertNotEquals(bigInt(1,1), bigInt(1,0))
    }

    @Test
    fun abs() {
        assertEquals(BigInt(11), BigInt(11).abs())
        assertEquals(BigInt(11), BigInt(-11).abs())
        assertEquals(BigInt(0), BigInt(0).abs())
    }

    @Test
    fun testBit() {
        assertFalse(BigInt("123456789022").testBit(0))
        assertFalse(BigInt("-2").testBit(0))
        assertFalse(BigInt("0").testBit(0))

        assertTrue(BigInt("12345678901").testBit(0))
        assertTrue(BigInt("1").testBit(0))
        assertTrue(BigInt("-1").testBit(0))
    }

    @Test
    fun toNumber() {
        assertEquals(0.0, BigInt("0").toDouble())
        assertEquals(0, BigInt("0").toInt())

        assertEquals(1234567890.0, BigInt("1234567890").toDouble())
        assertEquals(4000000000.0, BigInt("4000000000").toDouble())
        assertEquals(1234567890, BigInt("1234567890").toInt())

        assertEquals(-1234567890.0, BigInt("-1234567890").toDouble())
        assertEquals(-1234567890, BigInt("-1234567890").toInt())
    }

    @Test
    fun toNumberInvalid() {
        val invalid = listOf("3000000000", "-3000000000")
        invalid.forEach {
            val n = BigInt(it)
            assertFailsWith(ArithmeticException::class) { n.toInt() }
        }
    }

    @Test
    fun gcd() {
        assertEquals(BigInt(24), BigInt(24).gcd(BigInt(24)))
        assertEquals(BigInt(1), BigInt(25).gcd(BigInt(24)))
        assertEquals(BigInt(21), BigInt(1071).gcd(BigInt(462)))
        assertEquals(BigInt(6), BigInt(270).gcd(BigInt(-192)))
        assertEquals(BigInt(2), BigInt(-2).gcd(BigInt(2)))
        assertEquals(BigInt(229), BigInt("52139749485151463").gcd(BigInt("179883737510857067")))
    }

    @Test
    fun pow() {
        assertEquals(BigInt(1), BigInt(12345).pow(0))
        assertEquals(BigInt(12345), BigInt(12345).pow(1))
        assertEquals(BigInt(1), BigInt(1).pow(2147483647))
        assertEquals(BigInt(-1), BigInt(-1).pow(2147483647))

        assertEquals(BigInt(32), BigInt(2).pow(5))
        assertEquals(BigInt(-32), BigInt(-2).pow(5))
        assertEquals(BigInt("1267650600228229401496703205376"), BigInt(2).pow(100))
    }

    @Test
    fun mod() {
        assertEquals(BigInt(1), BigInt(7).mod(BigInt(3)))
        assertEquals(BigInt(2), BigInt(-7).mod(BigInt(3)))
    }

    @Test
    fun modPow() {
        val a = BigInt("1234567890987654321")
        val e = BigInt("1122334455667788990")
        val m = BigInt("5555555555555555555")

        assertEquals(BigInt("498317946897227631"), a.modPow(e, m))
    }

    @Test
    fun sqrtAndRemainder() {
        var sqrt = BigInt("10000000000000000000009").sqrtRemainder()
        assertEquals(BigInt("100000000000"), sqrt.first)
        assertEquals(BigInt("9"), sqrt.second)

        // Initial value is not determined by converting to double
        sqrt = BigInt("10000000000000000000000000000000000000009").sqrtRemainder()
        Assertions.assertEquals(BigInt("100000000000000000000"), sqrt.first)
        Assertions.assertEquals(BigInt("9"), sqrt.second)
    }

    /*
    @Test
    fun nthRootAndRemainder() {
        var root = BigInteger("1000000000000000000009").nthRootAndRemainder(3)
        Assertions.assertEquals(BigInteger("10000000"), root.first)
        Assertions.assertEquals(BigInteger("9"), root.second)

        root = BigInteger("16935003133160595268336552").nthRootAndRemainder(5)
        Assertions.assertEquals(BigInteger("111111"), root.first)
        Assertions.assertEquals(BigInteger("1"), root.second)

    }
     */
}