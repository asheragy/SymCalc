package org.cerion.math.bignum.integer

import kotlin.math.sign

@ExperimentalUnsignedTypes
abstract class BigIntArrayBase<T : BigIntArrayBase<T>> : BigInt<T> {
    protected abstract val sign: Byte
    protected abstract val arr: UIntArray

    companion object {
        const val POSITIVE = (1).toByte()
        const val NEGATIVE = (-1).toByte()
        const val ZEROSIGN = 0.toByte()
    }

    override fun add(other: T): T {
        return when {
            sign == ZEROSIGN -> other
            other.sign == ZEROSIGN -> this as T
            sign == other.sign -> getInstance(sign, add(this.arr, other.arr))

            // Subtract since one side is negative
            else ->
                when (BigIntArray.compare(arr, other.arr)) {
                    -1 -> getInstance((-1 * sign).toByte(), subtract(other.arr, arr))
                    1 -> getInstance(sign, subtract(arr, other.arr))
                    else -> getInstance(ZEROSIGN, UIntArray(0))
                }
        }
    }

    override fun subtract(other: T): T {
        if (sign == POSITIVE && other.sign == NEGATIVE)
            return getInstance(1, add(arr, other.arr))
        else if (sign == NEGATIVE && other.sign == POSITIVE)
            return getInstance(-1, add(arr, other.arr))
        else if (sign == ZEROSIGN)
            return other.negate()
        else if (other.sign == ZEROSIGN)
            return this as T

        // Sign is equal but need to subtract
        return when (BigIntArray.compare(arr, other.arr)) {
            -1 -> return getInstance((-1 * sign).toByte(), subtract(other.arr, arr))
            1 -> return getInstance(sign, subtract(arr, other.arr))
            else -> getZero()
        }
    }

    override fun multiply(other: T): T {
        if (sign == ZEROSIGN || other.sign == ZEROSIGN)
            return getZero()

        return getInstance(
            if(sign == other.sign) 1 else -1,
            when {
                arr.size == 1 -> BigIntArray.multiply(other.arr, arr[0])
                other.arr.size == 1 -> BigIntArray.multiply(arr, other.arr[0])
                else -> BigIntArray.multiply(arr, other.arr)
            }
        )
    }

    override fun pow(n: Int): T {
        return when(n.sign) {
            -1 -> throw UnsupportedOperationException("exponent cannot be negative")
            0 -> getOne()
            else -> {
                if (n == 1 || this == BigInt2.ONE)
                    return this as T

                // Negative One
                // TODO different check for this
                if (this == getNegativeOne())
                    return if(n % 2 == 0) this.negate() else this as T

                val resultSign = if(sign == (-1).toByte() && n % 2 == 1) -1 else 1
                return getInstance(resultSign.toByte(), BigIntArray.pow(arr, n))
            }
        }
    }

    override fun compareTo(other: T): Int {
        if (sign != other.sign)
            return sign.compareTo(other.sign)

        return when(sign) {
            NEGATIVE -> BigIntArray.compare(other.arr, arr)
            POSITIVE -> BigIntArray.compare(arr, other.arr)
            else -> 0
        }
    }

    override fun abs() = if (sign == NEGATIVE) getInstance(1, arr) else this as T
    override fun negate() = if (sign == ZEROSIGN) this as T else getInstance((-1 * sign).toByte(), arr)

    private fun getZero() = getInstance(ZEROSIGN, UIntArray(0))
    private fun getOne() = getInstance(1, UIntArray(1) { 1u })
    private fun getNegativeOne() = getInstance(-1, UIntArray(1) { 1u })

    abstract fun getInstance(sign: Byte, arr: UIntArray): T

    abstract fun add(x: UIntArray, y: UIntArray): UIntArray
    abstract fun subtract(x: UIntArray, y: UIntArray): UIntArray
    abstract fun multiply(x: UIntArray, y: UIntArray): UIntArray
    abstract fun multiply(x: UIntArray, y: UInt): UIntArray
    abstract fun pow(x: UIntArray, n: Int): UIntArray
}

