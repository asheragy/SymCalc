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
    override fun toString() = value.toString()

    override fun compareTo(other: TestInt): Int {
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<TestInt, TestInt> {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        return if (other is TestInt)
            if(is2) two == other.two else ten == other.ten
        else
            false
    }

    override fun hashCode() = value.hashCode()
}