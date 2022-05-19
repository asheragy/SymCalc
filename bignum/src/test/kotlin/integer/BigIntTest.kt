package org.cerion.math.bignum.integer

import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.test.*


@ExperimentalUnsignedTypes
internal class BigIntTest {
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
    fun fromInteger() = run {
        assertEquals("0", bigInt(0).toString())
        assertEquals("1", bigInt(1).toString())
        assertEquals("-1", bigInt(-1).toString())
        assertEquals("999999999", bigInt(999999999).toString())
        assertEquals("-999999999", bigInt(-999999999).toString())
        assertEquals("1000000000", bigInt(1000000000).toString())
        assertEquals("-1000000000", bigInt(-1000000000).toString())
        assertEquals("2147483647", bigInt(Int.MAX_VALUE).toString())
        assertEquals("-2147483648", bigInt(Int.MIN_VALUE).toString())
    }

    @Test
    fun fromLong() {
        // TODO BigInt2

        assertEquals("10000000000", BigInt10(10000000000).toString())
        assertEquals("9223372036854775807", BigInt10(Long.MAX_VALUE).toString())
        assertEquals("-9223372036854775808", BigInt10(Long.MIN_VALUE).toString())
    }

    @Test
    fun addition() = run {
        assertEquals(fromArray(1,0), fromArray(-1) + fromArray(1))

        // Digit carried
        assertEquals(fromArray(1,0,0), fromArray(-1,-1) + fromArray(1))
        assertEquals(fromArray(1,2,0), fromArray(1,1,-1) + fromArray(1))

        // Larger second
        assertEquals(fromArray(1,1,1,2), fromArray(1) + fromArray(1,1,1,1))
    }

    @Test
    fun subtract() = run {
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
    fun multiplySingleDigit() = run {
        assertEquals(fromArray(10,10,10), fromArray(1,1,1) * fromArray(10))
        assertEquals(fromArray(9,-1,-1,-10), fromArray(-1,-1,-1) * fromArray(10))
    }

    @Test
    fun multiply() = run {
        assertEquals(fromArray(1,2,3,2,1), fromArray(1,1,1) * fromArray(1,1,1))

        // Digits carried
        assertEquals(fromArray(-2,-1,-1,1), fromArray(-1,-1,-1) * fromArray(-1))
        assertEquals(fromArray(-1,-2,-1,0,1), fromArray(-1,-1,-1) * fromArray(-1, -1)) // y smaller than x
        assertEquals(fromArray(-1,-2,-1,0,1), fromArray(-1, -1) * fromArray(-1,-1,-1)) // x smaller than y
        assertEquals(fromArray(-1,-1,-2,0,0,1), fromArray(-1,-1,-1) * fromArray(-1,-1,-1))
    }

    @Test
    fun debug() {
        assertEquals(fromArray2(-1,-2,-1,0,1), fromArray2(-1,-1,-1) * fromArray2(-1, -1))
        //assertEquals(bigInt(-1,-1,-2,0,0,1), bigInt(-1,-1,-1) * bigInt(-1,-1,-1))
        //BigInteger("79228162514264337593543950335") * BigInteger("18446744073709551615")

        //assertEquals(BigInt("1683788257242160069488429204699054093792"), BigInt("15542256233227479069") * BigInt("108336153514341295968"))
    }

    @Test
    fun multiplySigned() = run {
        val zero = bigInt("0")
        val a = bigInt("100000000000")
        val abig = bigInt("10000000000000000000000")

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
    fun divisionSingleDigit_base2() {
        assertEquals(fromArray2(1,1,1,1), fromArray2(1,0,0,0,0).divide(BigInt2("4294967295")))
        assertEquals(fromArray2(1,1,1,1), fromArray2(-1,-1,-1,-1).divide(BigInt2("4294967295")))
        assertEquals(fromArray2(2147483647,-1,-1,-1), fromArray2(-1,-1,-1,-2).divide(BigInt2("2")))
    }

    @Test
    fun divisionSingleDigit_base10() {
        val result = fromArray10(1,0,0,1).divideAndRemainder(BigInt10("2"))
        assertEquals(fromArray10(500000000,0,0), result.first)
        assertEquals(fromArray10(1), result.second)

        assertEquals(fromArray10(1,1,1,1), fromArray10(1,0,0,0,0).divide(BigInt10("999999999")))
        assertEquals(fromArray10(1,1,1,1), fromArray10(-1,-1,-1,-1).divide(BigInt10("999999999")))
        assertEquals(fromArray10(499999999,-1,-1,-1), fromArray10(-1,-1,-1,-2).divide(BigInt10("2")))
    }

    @Test
    fun division() = run {
        assertEquals(Pair(fromArray(-1), ZERO), fromArray(-1,-1).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1), ONE), fromArray(1, 0, 0).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0), ZERO), fromArray(-1,-1,0).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0), fromArray(-1)), fromArray(-1,-1,-1).divideAndRemainder(fromArray(1,1)))
        assertEquals(Pair(fromArray(-1,0,-1), ZERO), fromArray(-1,-1,-1,-1).divideAndRemainder(fromArray(1,1)))

        assertEquals(Pair(bigInt("5384072782"), bigInt("12548692603041114205")),
            bigInt("75628712337421087763136048659").divideAndRemainder(bigInt("14046747766433220397")))
        assertEquals(Pair(bigInt("68"), bigInt("999999999999")),
            bigInt("7556555555555547").divideAndRemainder(bigInt("111111111111111")))
        assertEquals(Pair(bigInt("9000000000000"), bigInt("999999999999")),
            bigInt("999999999999999999999999999").divideAndRemainder(bigInt("111111111111111")))

        // Negative remainder
        assertEquals(Pair(bigInt("-24"), bigInt("-1")), bigInt("-49").divideAndRemainder(bigInt(2)))
        assertEquals(Pair(bigInt("-12"), bigInt("-2")), bigInt(-350).divideAndRemainder(bigInt(29)))
    }

    @Test
    fun divisionSigned() = run {
        assertEquals(bigInt(5), bigInt(25) / bigInt(5))
        assertEquals(bigInt(-5), bigInt(-25) / bigInt(5))
        assertEquals(bigInt(-5), bigInt(25) / bigInt(-5))
        assertEquals(bigInt(5), bigInt(-25) / bigInt(-5))
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
    fun compare() = run {
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
    fun abs() = run {
        assertEquals(bigInt(11), bigInt(11).abs())
        assertEquals(bigInt(11), bigInt(-11).abs())
        assertEquals(bigInt(0), bigInt(0).abs())
    }

    @Test
    fun testBit() = run {
        assertFalse(bigInt("123456789022").testBit(0))
        assertFalse(bigInt("-2").testBit(0))
        assertFalse(bigInt("0").testBit(0))

        assertTrue(bigInt("12345678901").testBit(0))
        assertTrue(bigInt("1").testBit(0))
        assertTrue(bigInt("-1").testBit(0))
    }

    @Test
    fun toNumber() = run {
        assertEquals(0.0, bigInt("0").toDouble())
        assertEquals(0, bigInt("0").toInt())

        assertEquals(1234567890.0, bigInt("1234567890").toDouble())
        assertEquals(4000000000.0, bigInt("4000000000").toDouble())
        assertEquals(1234567890, bigInt("1234567890").toInt())

        assertEquals(-1234567890.0, bigInt("-1234567890").toDouble())
        assertEquals(-1234567890, bigInt("-1234567890").toInt())

        assertEquals(123456789000000.0, bigInt("123456789000000").toDouble())
        assertEquals(-123456789000000.0, bigInt("-123456789000000").toDouble())
    }

    @Test
    fun toNumberInvalid() = run {
        val invalid = listOf("3000000000", "-3000000000")
        invalid.forEach {
            val n = bigInt(it)
            assertFailsWith(ArithmeticException::class) { n.toInt() }
        }
    }

    @Test
    fun gcd() = run {
        assertEquals(bigInt(24), bigInt(24).gcd(bigInt(24)))
        assertEquals(bigInt(1), bigInt(25).gcd(bigInt(24)))
        assertEquals(bigInt(21), bigInt(1071).gcd(bigInt(462)))
        assertEquals(bigInt(6), bigInt(270).gcd(bigInt(-192)))
        assertEquals(bigInt(2), bigInt(-2).gcd(bigInt(2)))
        assertEquals(bigInt(229), bigInt("52139749485151463").gcd(bigInt("179883737510857067")))
    }

    @Test
    fun pow() = run {
        assertEquals(bigInt(1), bigInt(12345).pow(0))
        assertEquals(bigInt(12345), bigInt(12345).pow(1))
        assertEquals(bigInt(1), bigInt(1).pow(2147483647))
        assertEquals(bigInt(-1), bigInt(-1).pow(2147483647))

        assertEquals(bigInt(32), bigInt(2).pow(5))
        assertEquals(bigInt(-32), bigInt(-2).pow(5))
        assertEquals(bigInt("1267650600228229401496703205376"), bigInt(2).pow(100))
    }

    @Test
    fun mod() = run {
        assertEquals(bigInt(1), bigInt(7).mod(bigInt(3)))
        assertEquals(bigInt(2), bigInt(-7).mod(bigInt(3)))
    }

    @Test
    fun modPow() = run {
        assertEquals(bigInt(1), bigInt(2).modPow(bigInt(10), bigInt(3)))
        assertEquals(bigInt(8), bigInt(5).modPow(bigInt(3), bigInt(13)))

        val a = bigInt("1234567890987654321")
        val e = bigInt("1122334455667788990")
        val m = bigInt("5555555555555555555")
        assertEquals(bigInt("498317946897227631"), a.modPow(e, m))
    }

    @Test
    fun bitShift_left() = run {
        assertEquals(bigInt("1"), bigInt("1").shiftLeft(0u))
        assertEquals(bigInt("2"), bigInt("1").shiftLeft(1u))
        assertEquals(bigInt("4294967296"), bigInt("1").shiftLeft(32u))
        assertEquals(bigInt("8589934592"), bigInt("4294967296").shiftLeft(1u))
        assertEquals(bigInt("102400"), bigInt("100").shiftLeft(10u))
        assertEquals(bigInt("102400"), bigInt("100").shiftLeft(10u))
        assertEquals(bigInt("102400"), bigInt("100").shiftLeft(10u))
        assertEquals(bigInt("295147905179352825856"), bigInt("16").shiftLeft(64u))
    }

    @Test
    fun bitShift_right() = run {
        assertEquals(bigInt("4294967296"), bigInt("4294967296").shiftRight(0u))
        assertEquals(bigInt("2147483648"), bigInt("4294967296").shiftRight(1u))
        assertEquals(bigInt("0"), bigInt("4294967296").shiftRight(99u))
        assertEquals(bigInt("4294967296"), bigInt("8589934592").shiftRight(1u))
        assertEquals(bigInt("291038304567"), bigInt("10000000000000000000000").shiftRight(35u))
        assertEquals(bigInt("323375893963"), bigInt("11111111111111111111111").shiftRight(35u))
    }

    @Test
    fun shiftRight10() {
        assertEquals(BigInt10("100000000"), BigInt10("1000000005").shiftRight10(1, false))
        assertEquals(BigInt10("100000001"), BigInt10("1000000005").shiftRight10(1))
        assertEquals(BigInt10("100000000"), BigInt10("1000000004").shiftRight10(1))

        assertEquals(BigInt10("999999999"), BigInt10("9999999999").shiftRight10(1, false))
        assertEquals(BigInt10("100000000"), BigInt10("9999999999").shiftRight10(1))

        assertEquals(BigInt10("0"), BigInt10("1").shiftRight10(1))
        assertEquals(BigInt10("0"), BigInt10("1").shiftRight10(10))
    }

    @Test
    fun shiftLeft10() {
        assertEquals(BigInt10.ZERO, BigInt10("0").shiftLeft10(10))
        assertEquals(BigInt10("10"), BigInt10("1").shiftLeft10(1))
        assertEquals(BigInt10("10000000000"), BigInt10("1").shiftLeft10(10))
    }

    @Test
    fun sqrtAndRemainder() = run {
        var sqrt = bigInt("10000000000000000000009").sqrtRemainder()
        assertEquals(bigInt("100000000000"), sqrt.first)
        assertEquals(bigInt("9"), sqrt.second)

        // Initial value is not determined by converting to double
        sqrt = bigInt("10000000000000000000000000000000000000009").sqrtRemainder()
        Assertions.assertEquals(bigInt("100000000000000000000"), sqrt.first)
        Assertions.assertEquals(bigInt("9"), sqrt.second)
    }

    @Test
    fun bitLength() = run {
        assertEquals(0u, bigInt(0).bitLength)
        assertEquals(1u, bigInt(1).bitLength)
        assertEquals(2u, bigInt(2).bitLength)
        assertEquals(32u, bigInt("4294967295").bitLength)
        assertEquals(33u, bigInt("4294967296").bitLength)
    }

    @Test
    fun digits() {
        assertEquals(1,BigInt10("0").digits)
        assertEquals(1,BigInt10("1").digits)
        assertEquals(1,BigInt10("-1").digits)

        assertEquals(9,BigInt10("999999999").digits)
        assertEquals(10,BigInt10("1000000000").digits)
        assertEquals(10,BigInt10("1000000001").digits)
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
        fun bigInt(n: Int) = TestInt(if(case == 0) BigInt2(n) else BigInt10(n))
        fun bigInt(value: String): TestInt = if(case == 0) TestInt(BigInt2(value)) else TestInt(BigInt10(value))
        val ZERO: TestInt
            get() = TestInt(if(case == 0) BigInt2.ZERO else BigInt10.ZERO)
        val ONE: TestInt
            get() = TestInt(if(case == 0) BigInt2.ONE else BigInt10.ONE)
        fun fromArray(vararg digits: Int): TestInt {
            if(case == 0)
                return TestInt(fromArray2(*digits))

            return TestInt(fromArray10(*digits))
        }
    }

    companion object {
        fun fromArray10(vararg digits: Int): BigInt10 {
            val arr = digits.map {
                if (it < 0)
                    (1000000000 + it).toUInt()
                else
                    it.toUInt()
            }.reversed().toUIntArray()

            return BigInt10(1, arr)
        }

        fun fromArray2(vararg n: Int): BigInt2 = BigInt2(1, n.map { it.toUInt() }.reversed().toUIntArray())
    }
}