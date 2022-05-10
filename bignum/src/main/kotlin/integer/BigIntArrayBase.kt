package org.cerion.math.bignum.integer

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
        TODO("Not yet implemented")
    }

    abstract fun getInstance(sign: Byte, arr: UIntArray): T

    abstract fun add(x: UIntArray, y: UIntArray): UIntArray
    abstract fun subtract(x: UIntArray, y: UIntArray): UIntArray
}

