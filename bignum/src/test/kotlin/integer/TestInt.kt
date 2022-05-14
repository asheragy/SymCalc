package org.cerion.math.bignum.integer

@ExperimentalUnsignedTypes
class TestInt(private val value: BigInt<*>) : BigInt<TestInt> {
    private val is2: Boolean
        get() = value is BigInt2
    val two: BigInt2
        get() = value as BigInt2
    val ten: BigInt10
        get() = value as BigInt10

    override fun add(other: TestInt) = TestInt(if(is2) two.add(other.two) else ten.add(other.ten))
    override fun subtract(other: TestInt) = TestInt(if(is2) two.subtract(other.two) else ten.subtract(other.ten))
    override fun multiply(other: TestInt) = TestInt(if(is2) two.multiply(other.two) else ten.multiply(other.ten))
    override fun divide(other: TestInt) = TestInt(if(is2) two.divide(other.two) else ten.divide(other.ten))
    override fun divideAndRemainder(other: TestInt): Pair<TestInt, TestInt> {
        val result = if(is2) two.divideAndRemainder(other.two) else ten.divideAndRemainder(other.ten)
        return Pair(TestInt(result.first), TestInt(result.second))
    }
    override fun sqrtRemainder(): Pair<TestInt, TestInt> {
        val result = if(is2) two.sqrtRemainder() else ten.sqrtRemainder()
        return Pair(TestInt(result.first), TestInt(result.second))
    }

    override fun gcd(n: TestInt) = TestInt(if(is2) two.gcd(n.two) else ten.gcd(n.ten))
    override fun pow(n: Int) = TestInt(if(is2) two.pow(n) else ten.pow(n))
    override fun modPow(exponent: TestInt, m: TestInt) = TestInt(if(is2) two.modPow(exponent.two, m.two) else ten.modPow(exponent.ten, m.ten))
    override fun mod(m: TestInt) = TestInt(if(is2) two.mod(m.two) else ten.mod(m.ten))


    override fun toString() = value.toString()

    override fun compareTo(other: TestInt) = if(is2) two.compareTo(other.two) else ten.compareTo(other.ten)


    override fun negate() = TestInt(if(is2) two.negate() else ten.negate())
    override fun abs() = TestInt(if(is2) two.abs() else ten.abs())
    override fun toDouble() = if(is2) two.toDouble() else ten.toDouble()
    override fun toInt() = if(is2) two.toInt() else ten.toInt()
    override fun signum(): Int {
        TODO("Not yet implemented")
    }


    override fun testBit(n: Int) = if(is2) two.testBit(n) else ten.testBit(n)
    override val bitLength: UInt
        get() = if(is2) two.bitLength else ten.bitLength

    override fun equals(other: Any?): Boolean {
        return if (other is TestInt)
            if(is2) two == other.two else ten == other.ten
        else
            false
    }

    override fun shiftRight(n: UInt) = TestInt(if(is2) two.shiftRight(n) else ten.shiftRight(n))
    override fun shiftLeft(n: UInt) = TestInt(if(is2) two.shiftLeft(n) else ten.shiftLeft(n))

    override fun hashCode() = value.hashCode()
}