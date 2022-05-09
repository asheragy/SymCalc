package org.cerion.math.bignum.integer

@ExperimentalUnsignedTypes
class BigInt10 : BigInt<BigInt10> {
    private val sign: Byte
    private val arr: UIntArray

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
        TODO("Not yet implemented")
    }

    override fun compareTo(other: BigInt10): Int {
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<BigInt10, BigInt10> {
        TODO("Not yet implemented")
    }
}