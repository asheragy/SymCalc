package org.cerion.math.bignum.integer

import org.cerion.math.bignum.integer.toShiftedUInt
import java.lang.RuntimeException

private operator fun UIntArray.compareTo(other: UIntArray): Int = BigIntArray.compare(this, other)

@ExperimentalUnsignedTypes
object BigIntArray {

    internal fun compare(x: UIntArray, y: UIntArray): Int {
        val size = x.size.compareTo(y.size)
        if (size != 0)
            return size

        // Start at most significant digit
        for (i in x.size - 1 downTo 0)
            if (x[i].compareTo(y[i]) != 0)
                return x[i].compareTo(y[i])

        return 0
    }

    internal fun add(x: UIntArray, y: UIntArray): UIntArray {
        val a = if (x.size >= y.size) x else y
        val b = if (x.size >= y.size) y else x

        val arr = UIntArray(a.size)
        var index = 0
        var sum: ULong = 0uL

        // Add digits while array lengths are the same
        while(index < b.size) {
            sum = a[index].toULong() + b[index].toULong() + (sum shr 32)
            arr[index++] = sum.toUInt()
        }

        // Add digits for larger number while digit is still carried
        var carry = sum shr 32 != 0uL
        while(index < a.size && carry) {
            sum = a[index].toULong() + (sum shr 32)
            carry = sum shr 32 != 0uL
            arr[index++] = sum.toUInt()
        }

        // If there is still a digit to carry add it and return
        if (carry) {
            val t = arr.copyOf(arr.size + 1)
            t[arr.size] = 1u
            return t
        }

        // If no more carry just add the remaining digits
        while(index < a.size)
            arr[index] = a[index++]

        return arr
    }

    internal fun subtract(x: UIntArray, y: UInt): UIntArray {
        // TODO implement this without calling the other for slight efficiency
        return subtract(x, UIntArray(1) { y })
    }

    internal fun subtract(x: UIntArray, y: UIntArray): UIntArray {
        if(compare(x,y) < 0)
            throw ArithmeticException("invalid subtraction") // TODO remove temp error checking

        // Input is always expected to have x as the largest array and adjust result sign accordingly
        val arr = UIntArray(x.size) // TODO is there a benefit in copying starting array? need to performance test first
        var index = 0
        var diff = 0L

        while(index < y.size) {
            diff = x[index].toLong() - y[index].toLong() + (diff shr 32)
            arr[index++] = diff.toUInt()
        }

        var borrow = diff < 0
        while(index < x.size && borrow) {
            diff = x[index].toLong() - 1
            borrow = diff < 0
            arr[index++] = diff.toUInt()
        }

        while(index < x.size)
            arr[index] = x[index++]

        return removeLeadingZeros(arr)
    }

    internal fun multiply(x: UIntArray, y: UInt): UIntArray {
        val result = UIntArray(x.size + 1) // Allocate carried digit by default
        var t = 0uL
        var i = 0

        while(i < x.size) {
            t = (x[i].toULong() * y) + t.toShiftedUInt()
            result[i++] = t.toUInt()
        }

        result[x.size] = t.toShiftedUInt()
        return removeLeadingZeros(result)
    }

    internal fun multiply(x: UIntArray, y: UIntArray): UIntArray {
        val xlen = x.size
        val ylen = y.size
        val z = UIntArray(xlen + ylen)

        var carry = 0uL
        var j = 0
        while (j < ylen) {
            try {
                val product = y[j].toULong() * x[0].toULong() + carry
                z[j] = product.toUInt()
                carry = product shr 32
                j++
            }
            catch(e: Exception) {
                println("TEST")
            }
        }

        z[ylen] = carry.toUInt()

        var i = 1
        while (i < xlen) {
            carry = 0uL
            j = 0
            var k = i
            while (j < ylen) {
                val product = y[j].toULong() * x[i].toULong() + z[k].toULong() + carry
                z[k] = product.toUInt()
                carry = product shr 32
                j++
                k++
            }
            z[ylen + i] = carry.toUInt()
            i++
        }

        return removeLeadingZeros(z)
    }

    internal fun pow(x: UIntArray, n: Int): UIntArray {
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

    /**
     * Division with only first significant digit of divisor
     * ex: 88888/777 --> 88800/700 --> 888/7
     */
    private fun fastDivide(n: UIntArray, d: UIntArray): UIntArray {
        if (d.size > n.size)
            return UIntArray(0)

        val offset = d.size - 1
        val y = d.last()
        // For reference this is what needs to be computed
        //val n1 = n.toList().drop(offset).toUIntArray()
        //return  divideAndRemainder(n1, y).first

        var index = n.size - 1
        var r = if (n.last() / y == 0u)
            (n[index--].toULong() shl 32)
        else
            0uL

        val result = UIntArray(index+1-offset)
        while(index >= offset) {
            val t = r + n[index]
            result[index-offset] = (t / y).toUInt()
            r = (t % y) shl 32
            index--
        }

        return removeLeadingZeros(result)
    }

    // http://justinparrtech.com/JustinParr-Tech/an-algorithm-for-arbitrary-precision-integer-division/
    internal fun divide(n: UIntArray, d: UIntArray): Pair<UIntArray,UIntArray?> {
        // Assumes n > d
        if (compare(n, d) < 0)
            throw RuntimeException("numerator must be greater than denominator")

        var q = fastDivide(n, d)

        while(true) {
            var qd = multiply(q, d)
            val comp = compare(qd, n)
            if (comp == 0) // Exact, no remainder
                return Pair(q,null)

            // This indicates if remainder is negative or not
            var r = if (comp == 1) subtract(qd, n) else subtract(n,qd)
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
                qd = multiply(q,d)

                if (n > qd) {
                    r = subtract(n,qd)
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

    internal fun divideAndRemainder(x: UIntArray, y: UInt): Pair<UIntArray, UInt> {
        var index = x.size - 1
        var r = if (x.last() / y == 0u)
            (x[index--].toULong() shl 32)
        else
            0uL

        val result = UIntArray(index+1)

        while(index >= 0) {
            val t = r + x[index]
            result[index] = (t / y).toUInt()
            r = (t % y) shl 32
            index--
        }

        return Pair(removeLeadingZeros(result), r.toShiftedUInt())
    }

    private fun removeLeadingZeros(arr: UIntArray): UIntArray {
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