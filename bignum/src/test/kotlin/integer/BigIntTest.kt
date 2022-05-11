package org.cerion.math.bignum.integer

import org.junit.Test
import org.junit.jupiter.api.Assertions
import kotlin.test.*


@ExperimentalUnsignedTypes
internal class BigIntTest {

    // TODO make BigInt2Test for these since 10 will use a different variation
    private fun fromArray(vararg n: Int): BigInt2 = BigInt2(1, n.map { it.toUInt() }.reversed().toUIntArray())

    @Test
    fun parse() = run {
        // Tests to and from string
        assertEquals("1", bigInt("1").toString())
        assertEquals("1", bigInt("0001").toString())
        assertEquals("-1", bigInt("-0001").toString())
        assertEquals("2000000000", bigInt("2000000000").toString())
        assertEquals("-2000000000", bigInt("-2000000000").toString())
        assertEquals("4000000000", bigInt("4000000000").toString())
        assertEquals("4294967295", bigInt("4294967295").toString())
        assertEquals("4294967296", bigInt("4294967296").toString())
        assertEquals("18446744078004518913", bigInt("18446744078004518913").toString())
        assertEquals("36893488147419103231", bigInt("36893488147419103231").toString())
        assertEquals("79228162514264337593543950335", bigInt("79228162514264337593543950335").toString())
        assertEquals("100000000000000000000000000000000", bigInt("100000000000000000000000000000000").toString())

        // toString requires addition carry
        assertEquals("10000000000000000000000000", bigInt("10000000000000000000000000").toString())

        // Multiple addition carries at the end
        assertEquals("340282366920938463463374607431768211456", bigInt("340282366920938463463374607431768211456").toString())
    }

    @Test
    fun addition() {
        assertEquals(fromArray(1,0), fromArray(-1) + fromArray(1))

        // Digit carried
        assertEquals(fromArray(1,0,0), fromArray(-1,-1) + fromArray(1))
        assertEquals(fromArray(1,2,0), fromArray(1,1,-1) + fromArray(1))

        // Larger second
        assertEquals(fromArray(1,1,1,2), fromArray(1) + fromArray(1,1,1,1))
    }

    @Test
    fun subtract() {
        assertEquals(fromArray(2,2), fromArray(3,3) - fromArray(1,1))

        // Digits borrowed
        assertEquals(fromArray(4,-1), fromArray(5,1) - fromArray(2))
        assertEquals(fromArray(3,-4), fromArray(5,1) - fromArray(1,5))
        assertEquals(fromArray(1,-2,-1), fromArray(2,0,1) - fromArray(1,2))
        assertEquals(fromArray(1,3,-4), fromArray(1,5,1) - fromArray(1,5))

        // Result shrinks in size
        assertEquals(fromArray(1), fromArray(1,1,1,2) - fromArray(1,1,1,1))

        // Negative
        assertEquals(fromArray(2,2).negate(), fromArray(1,1) - fromArray(3,3))
    }

    @Test
    fun additionSigned() = run {
        assertEquals(bigInt("10000000000"), bigInt("9999999999") + bigInt("1"))
        assertEquals(bigInt("9999999999"), bigInt("10000000000") + bigInt("-1"))

        assertEquals(bigInt("10000000000"), bigInt("21111111111") + bigInt("-11111111111"))
        assertEquals(bigInt("-20000000000"), bigInt("21111111111") + bigInt("-41111111111"))

        assertEquals(bigInt("-10000000000"), bigInt("-21111111111") + bigInt("11111111111"))
        assertEquals(bigInt("30000000000"), bigInt("-21111111111") + bigInt("51111111111"))

        assertEquals(bigInt("-72222222222"), bigInt("-21111111111") + bigInt("-51111111111"))
    }

    @Test
    fun subtractionSigned() = run {
        // One negative (internal addition)
        assertEquals(bigInt("32222222222"), bigInt("21111111111") - bigInt("-11111111111"))
        assertEquals(bigInt("-32222222222"), bigInt("-21111111111") - bigInt("11111111111"))

        // Both negative
        assertEquals(bigInt("-10000000000"), bigInt("-21111111111") - bigInt("-11111111111"))
        assertEquals(bigInt("30000000000"), bigInt("-21111111111") - bigInt("-51111111111"))
    }

    @Test
    fun multiplySingleDigit() {
        assertEquals(fromArray(10,10,10), fromArray(1,1,1) * fromArray(10))
        assertEquals(fromArray(9,-1,-1,-10), fromArray(-1,-1,-1) * fromArray(10))
    }

    @Test
    fun multiply() {
        assertEquals(fromArray(1,2,3,2,1), fromArray(1,1,1) * fromArray(1,1,1))

        // Digits carried
        assertEquals(fromArray(-2,-1,-1,1), fromArray(-1,-1,-1) * fromArray(-1))
        assertEquals(fromArray(-1,-2,-1,0,1), fromArray(-1,-1,-1) * fromArray(-1, -1)) // y smaller than x
        assertEquals(fromArray(-1,-2,-1,0,1), fromArray(-1, -1) * fromArray(-1,-1,-1)) // x smaller than y
        assertEquals(fromArray(-1,-1,-2,0,0,1), fromArray(-1,-1,-1) * fromArray(-1,-1,-1))
    }

    @Test
    fun debug() {
        assertEquals(fromArray(-1,-2,-1,0,1), fromArray(-1,-1,-1) * fromArray(-1, -1))
        //assertEquals(bigInt(-1,-1,-2,0,0,1), bigInt(-1,-1,-1) * bigInt(-1,-1,-1))
        //BigInteger("79228162514264337593543950335") * BigInteger("18446744073709551615")

        //assertEquals(BigInt("1683788257242160069488429204699054093792"), BigInt("15542256233227479069") * BigInt("108336153514341295968"))
    }

    @Test
    fun multiplySigned() {
        val zero = BigInt2.ZERO
        val a = BigInt2("100000000000")
        val abig = BigInt2("10000000000000000000000")

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
        assertEquals(fromArray(1,1,1,1), fromArray(1,0,0,0,0).divide(4294967295u))
        assertEquals(fromArray(1,1,1,1), fromArray(-1,-1,-1,-1).divide(4294967295u))
        assertEquals(fromArray(2147483647,-1,-1,-1), fromArray(-1,-1,-1,-2).divide(2u))
    }

    @Test
    fun division() {
        assertEquals(Pair(fromArray(-1), BigInt2.ZERO), fromArray(-1,-1).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1), BigInt2.ONE), fromArray(1, 0, 0).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0), BigInt2.ZERO), fromArray(-1,-1,0).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0), fromArray(-1)), fromArray(-1,-1,-1).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0,-1), BigInt2.ZERO), fromArray(-1,-1,-1,-1).divideAndRemainder(fromArray(1,1)))

        assertEquals(Pair(BigInt2("5384072782"), BigInt2("12548692603041114205")), BigInt2("75628712337421087763136048659").divideAndRemainder(
            BigInt2("14046747766433220397")
        ))
        assertEquals(Pair(BigInt2("68"), BigInt2("999999999999")), BigInt2("7556555555555547").divideAndRemainder(
            BigInt2("111111111111111")
        ))
        assertEquals(Pair(BigInt2("9000000000000"), BigInt2("999999999999")), BigInt2("999999999999999999999999999").divideAndRemainder(
            BigInt2("111111111111111")
        ))

        // Negative remainder
        assertEquals(Pair(BigInt2("-24"), BigInt2("-1")), BigInt2("-49").divideAndRemainder(BigInt2(2)))
        assertEquals(Pair(BigInt2("-12"), BigInt2("-2")), BigInt2(-350).divideAndRemainder(BigInt2(29)))
    }

    @Test
    fun divisionSigned() {
        assertEquals(BigInt2(5), BigInt2(25) / BigInt2(5))
        assertEquals(BigInt2(-5), BigInt2(-25) / BigInt2(5))
        assertEquals(BigInt2(-5), BigInt2(25) / BigInt2(-5))
        assertEquals(BigInt2(5), BigInt2(-25) / BigInt2(-5))
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
        assertTrue(fromArray(1,2) > fromArray(9))
        assertTrue(fromArray(9) < fromArray(1,0))

        // Negative is opposite
        assertTrue(fromArray(1,2).negate() < fromArray(9))
        assertTrue(fromArray(9) > fromArray(1,0).negate())

        // Equal
        assertEquals(fromArray(1,1), fromArray(1,1))

        // Not Equal
        assertNotEquals(fromArray(1,1), fromArray(1,1).negate())
        assertNotEquals(fromArray(1,1), fromArray(1,1,1))
        assertNotEquals(fromArray(1,1), fromArray(1,0))
    }

    @Test
    fun abs() {
        assertEquals(BigInt2(11), BigInt2(11).abs())
        assertEquals(BigInt2(11), BigInt2(-11).abs())
        assertEquals(BigInt2(0), BigInt2(0).abs())
    }

    @Test
    fun testBit() {
        assertFalse(BigInt2("123456789022").testBit(0))
        assertFalse(BigInt2("-2").testBit(0))
        assertFalse(BigInt2("0").testBit(0))

        assertTrue(BigInt2("12345678901").testBit(0))
        assertTrue(BigInt2("1").testBit(0))
        assertTrue(BigInt2("-1").testBit(0))
    }

    @Test
    fun toNumber() {
        assertEquals(0.0, BigInt2("0").toDouble())
        assertEquals(0, BigInt2("0").toInt())

        assertEquals(1234567890.0, BigInt2("1234567890").toDouble())
        assertEquals(4000000000.0, BigInt2("4000000000").toDouble())
        assertEquals(1234567890, BigInt2("1234567890").toInt())

        assertEquals(-1234567890.0, BigInt2("-1234567890").toDouble())
        assertEquals(-1234567890, BigInt2("-1234567890").toInt())
    }

    @Test
    fun toNumberInvalid() {
        val invalid = listOf("3000000000", "-3000000000")
        invalid.forEach {
            val n = BigInt2(it)
            assertFailsWith(ArithmeticException::class) { n.toInt() }
        }
    }

    @Test
    fun gcd() {
        assertEquals(BigInt2(24), BigInt2(24).gcd(BigInt2(24)))
        assertEquals(BigInt2(1), BigInt2(25).gcd(BigInt2(24)))
        assertEquals(BigInt2(21), BigInt2(1071).gcd(BigInt2(462)))
        assertEquals(BigInt2(6), BigInt2(270).gcd(BigInt2(-192)))
        assertEquals(BigInt2(2), BigInt2(-2).gcd(BigInt2(2)))
        assertEquals(BigInt2(229), BigInt2("52139749485151463").gcd(BigInt2("179883737510857067")))
    }

    @Test
    fun pow() {
        assertEquals(BigInt2(1), BigInt2(12345).pow(0))
        assertEquals(BigInt2(12345), BigInt2(12345).pow(1))
        assertEquals(BigInt2(1), BigInt2(1).pow(2147483647))
        assertEquals(BigInt2(-1), BigInt2(-1).pow(2147483647))

        assertEquals(BigInt2(32), BigInt2(2).pow(5))
        assertEquals(BigInt2(-32), BigInt2(-2).pow(5))
        assertEquals(BigInt2("1267650600228229401496703205376"), BigInt2(2).pow(100))
    }

    @Test
    fun mod() {
        assertEquals(BigInt2(1), BigInt2(7).mod(BigInt2(3)))
        assertEquals(BigInt2(2), BigInt2(-7).mod(BigInt2(3)))
    }

    @Test
    fun modPow() {
        val a = BigInt2("1234567890987654321")
        val e = BigInt2("1122334455667788990")
        val m = BigInt2("5555555555555555555")

        assertEquals(BigInt2("498317946897227631"), a.modPow(e, m))
    }

    @Test
    fun sqrtAndRemainder() {
        var sqrt = BigInt2("10000000000000000000009").sqrtRemainder()
        assertEquals(BigInt2("100000000000"), sqrt.first)
        assertEquals(BigInt2("9"), sqrt.second)

        // Initial value is not determined by converting to double
        sqrt = BigInt2("10000000000000000000000000000000000000009").sqrtRemainder()
        Assertions.assertEquals(BigInt2("100000000000000000000"), sqrt.first)
        Assertions.assertEquals(BigInt2("9"), sqrt.second)
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

    private fun run(block: TestScope.() -> Unit) {
        for(i in 0..1) {
            TestScope(i).block()
        }
    }

    private class TestScope(val case: Int) {
        fun bigInt(value: String): TestInt = if(case == 0) TestInt(BigInt2(value)) else TestInt(BigInt10(value))
    }
}