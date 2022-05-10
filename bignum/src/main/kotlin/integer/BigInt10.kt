package org.cerion.math.bignum.integer

@ExperimentalUnsignedTypes
class BigInt10 : BigInt<BigInt10> {
    private val sign: Byte
    private val arr: UIntArray

    companion object {
        val ZERO = BigInt10(0, UIntArray(0))
        val ONE = BigInt10(1, UIntArray(1) { 1u })
        val NEGATIVE_ONE = BigInt10(-1, UIntArray(1) { 1u })
    }

    constructor(s: Int, arr: UIntArray) : this (s.toByte(), arr)
    constructor(sign: Byte, arr: UIntArray) {
        this.sign = sign
        this.arr = arr
    }
    constructor(str: String) {
        val n = str.trimStart('-', '0')
        if (n.isEmpty()) {
            this.arr = UIntArray(0)
            this.sign = BigInt.ZEROSIGN
            return
        }

        sign = if(str[0] == '-') BigInt.NEGATIVE else BigInt.POSITIVE

        // TODO this speed could be improved
        // declare array with exact size
        // loop in chunks of string length
        val digits = n.reversed().chunked(9).map { Integer.parseInt(it.reversed()).toUInt() }
        this.arr = digits.toUIntArray()
    }

    override fun toString(): String {
        if (sign == BigInt.ZEROSIGN)
            return "0"

        val sb = StringBuilder()
        if (sign == BigInt.NEGATIVE)
            sb.append("-")

        sb.append(arr[arr.size-1])

        for(i in arr.size-2 downTo  0) {
            val t = arr[i].toString()
            sb.append("0".repeat(9 - t.length) + t)
        }

        return sb.toString()
    }

    override fun add(other: BigInt10): BigInt10 {
        return when {
            sign == BigInt.ZEROSIGN -> other
            other.sign == BigInt.ZEROSIGN -> this
            sign == other.sign -> BigInt10(sign, BigIntArray.add10(this.arr, other.arr))

            // Subtract since one side is negative
            else ->
                when (BigIntArray.compare(arr, other.arr)) {
                    -1 -> BigInt10(-1 * sign, BigIntArray.subtract10(other.arr, arr))
                    1 -> BigInt10(sign, BigIntArray.subtract10(arr, other.arr))
                    else -> ZERO
            }
        }
    }

    override fun equals(other: Any?) = other is BigInt10 && sign == other.sign && arr.contentEquals(other.arr)

    override fun compareTo(other: BigInt10): Int {
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<BigInt10, BigInt10> {
        TODO("Not yet implemented")
    }
}