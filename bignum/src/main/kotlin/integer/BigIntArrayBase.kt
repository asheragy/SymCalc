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
                when (compare(arr, other.arr)) {
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
        return when (compare(arr, other.arr)) {
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
                arr.size == 1 -> multiply(other.arr, arr[0])
                other.arr.size == 1 -> multiply(arr, other.arr[0])
                else -> multiply(arr, other.arr)
            }
        )
    }

    override fun divide(other: T) = divideAndRemainder(other).first

    override fun divideAndRemainder(other: T): Pair<T, T> {
        var sign = (sign * other.sign).toByte()

        if (other.arr.size == 1) {
            val result = divide(arr, other.arr[0])
            val dividend = getInstance(if(result.first.isEmpty()) ZEROSIGN else sign, result.first)
            // TODO should remainder sign be negative?
            val remainder = getInstance(sign, UIntArray(1) { result.second })

            return Pair(dividend, remainder)
        }

        // TODO replace compare with overloaded operator
        val div = if (arr < other.arr) // x/y = 0 rem x when x<y
            Pair(UIntArray(0), arr)
        else
            divide(this.arr, other.arr)

        val rem = when {
            div.second == null -> getZero()
            sign == (-1).toByte() -> getInstance(NEGATIVE, div.second!!)
            else -> getInstance(POSITIVE, div.second!!)
        }

        if (div.first.isEmpty())
            sign = 0

        return Pair(getInstance(sign, div.first), rem)
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
                return getInstance(resultSign.toByte(), pow(arr, n))
            }
        }
    }

    override fun gcd(n: T): T {
        if (this.sign == ZEROSIGN)
            return n
        else if (n.sign == ZEROSIGN)
            return this as T

        // A is the larger of the two numbers
        var a: UIntArray
        var b: UIntArray
        when(compare(arr, n.arr)) {
            1 -> { a = arr; b = n.arr }
            -1 -> { b = arr; a = n.arr }
            else -> return this.abs()
        }

        var c = divide(a, b)

        // No remainder, GCD is B
        while (c.second != null) {
            a = b
            b = c.second!!
            c = divide(a, b)
        }

        return getInstance(1, b)
    }

    override fun mod(m: T): T {
        val rem = this.divideAndRemainder(m).second

        if (rem.sign == NEGATIVE)
            return m + rem

        return rem
    }

    override fun compareTo(other: T): Int {
        if (sign != other.sign)
            return sign.compareTo(other.sign)

        return when(sign) {
            NEGATIVE -> compare(other.arr, arr)
            POSITIVE -> compare(arr, other.arr)
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        // TODO not technically correct but both subclasses are not intended to be used together
        if (other is BigIntArrayBase<*>)
            return sign == other.sign && arr.contentEquals(other.arr)

        return false
    }

    override fun hashCode(): Int {
        var result = sign.toInt()
        result = 31 * result + arr.hashCode()
        return result
    }

    override fun abs() = if (sign == NEGATIVE) getInstance(1, arr) else this as T
    override fun negate() = if (sign == ZEROSIGN) this as T else getInstance((-1 * sign).toByte(), arr)

    private fun getZero() = getInstance(ZEROSIGN, UIntArray(0))
    private fun getOne() = getInstance(1, UIntArray(1) { 1u })
    private fun getNegativeOne() = getInstance(-1, UIntArray(1) { 1u })

    abstract fun getInstance(sign: Byte, arr: UIntArray): T

    protected abstract fun add(x: UIntArray, y: UIntArray): UIntArray
    protected abstract fun subtract(x: UIntArray, y: UIntArray): UIntArray
    protected abstract fun multiply(x: UIntArray, y: UIntArray): UIntArray
    protected abstract fun multiply(x: UIntArray, y: UInt): UIntArray
    protected abstract fun divide(x: UIntArray, y: UInt): Pair<UIntArray, UInt>
    protected abstract fun fastDivide(n: UIntArray, d: UIntArray): UIntArray

    private fun subtract(x: UIntArray, y: UInt): UIntArray {
        // TODO implement this without calling the other for slight efficiency
        return subtract(x, UIntArray(1) { y })
    }

    // http://justinparrtech.com/JustinParr-Tech/an-algorithm-for-arbitrary-precision-integer-division/
    private fun divide(n: UIntArray, d: UIntArray): Pair<UIntArray,UIntArray?> {
        // Assumes n > d
        if (n < d)
            throw RuntimeException("numerator must be greater than denominator")

        var q = fastDivide(n, d)

        while(true) {
            var qd = multiply(q, d)
            val comp = compare(qd, n)
            if (comp == 0) // Exact, no remainder
                return Pair(q,null)

            // This indicates if remainder is negative or not
            var r = if (comp == 1) subtract(qd, n) else subtract(n, qd)
            // TODO add Qn

            val ra = fastDivide(r, d)
            if (ra.isNotEmpty()) {
                q = if (comp == -1)
                    add(q, ra)
                else
                    subtract(q, ra)
            }

            if (r < d) {
                // Done, get latest R based on Q
                qd = multiply(q, d)

                if (n > qd) {
                    r = subtract(n, qd)
                }
                else {
                    r = subtract(qd, n)
                    // Remainder is negative, make it positive and remove 1 from quotient
                    q = subtract(q, 1u)
                    r = subtract(d, r)
                }

                return Pair(q, r)
            }
        }
    }

    protected fun pow(x: UIntArray, n: Int): UIntArray {
        var result = UIntArray(1) { 1u }
        var square = x
        var m = n
        while(m > 0) {
            if (m % 2 == 1)
                result = multiply(square, result)

            square = multiply(square, square)
            m /= 2
        }

        return result
    }

    protected operator fun UIntArray.compareTo(other: UIntArray): Int = compare(this, other)
    protected fun compare(x: UIntArray, y: UIntArray): Int {
        val size = x.size.compareTo(y.size)
        if (size != 0)
            return size

        // Start at most significant digit
        for (i in x.size - 1 downTo 0)
            if (x[i].compareTo(y[i]) != 0)
                return x[i].compareTo(y[i])

        return 0
    }

    protected fun removeLeadingZeros(arr: UIntArray): UIntArray {
        var digits = 0
        for(i in arr.size -1 downTo 0) {
            if (arr[i] == 0u)
                digits++
            else
                break
        }

        if (digits > 0) // TODO, this drops elements from end of array is there a way to do that without copy?
            return arr.copyOf(arr.size - digits)

        return arr
    }
}

