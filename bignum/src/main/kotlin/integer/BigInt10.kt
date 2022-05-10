package org.cerion.math.bignum.integer

@ExperimentalUnsignedTypes
class BigInt10 : BigIntArrayBase<BigInt10> {
    override val sign: Byte
    override val arr: UIntArray

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
            this.sign = ZEROSIGN
            return
        }

        sign = if(str[0] == '-') NEGATIVE else POSITIVE

        // TODO this speed could be improved
        // declare array with exact size
        // loop in chunks of string length
        val digits = n.reversed().chunked(9).map { Integer.parseInt(it.reversed()).toUInt() }
        this.arr = digits.toUIntArray()
    }

    override fun subtract(other: BigInt10): BigInt10 {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        if (sign == ZEROSIGN)
            return "0"

        val sb = StringBuilder()
        if (sign == NEGATIVE)
            sb.append("-")

        sb.append(arr[arr.size-1])

        for(i in arr.size-2 downTo  0) {
            val t = arr[i].toString()
            sb.append("0".repeat(9 - t.length) + t)
        }

        return sb.toString()
    }

    override fun getInstance(sign: Byte, arr: UIntArray) = BigInt10(sign, arr)

    override fun equals(other: Any?) = other is BigInt10 && sign == other.sign && arr.contentEquals(other.arr)

    override fun compareTo(other: BigInt10): Int {
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<BigInt10, BigInt10> {
        TODO("Not yet implemented")
    }

    override fun add(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.add10(x, y)
    override fun subtract(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.subtract10(x, y)
}