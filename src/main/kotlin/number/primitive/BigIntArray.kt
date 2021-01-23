package org.cerion.symcalc.number.primitive

object BigIntArray {

    internal fun compare(x: IntArray, y: IntArray): Int {
        val size = x.size.compareTo(y.size)
        if (size != 0)
            return size

        // Start at most significant digit
        for (i in x.size - 1 downTo 0)
            if (x[i].compareTo(y[i]) != 0)
                return x[i].compareTo(y[i])

        return 0
    }

    internal fun add(x: IntArray, y: IntArray): IntArray {
        val a = if (x.size >= y.size) x else y
        val b = if (a === x) y else x

        val arr = IntArray(a.size)
        var index = 0
        var sum: Long = 0

        // Add digits while array lengths are the same
        while(index < b.size) {
            sum = (a[index].toLong() and LONG_MASK) + (b[index].toLong() and LONG_MASK) + (sum ushr 32)
            arr[index++] = sum.toInt()
        }

        // Add digits for larger number while digit is still carried
        var carry = sum ushr 32 != 0L
        while(index < a.size && carry) {
            sum = (a[index].toLong() and LONG_MASK) + (sum ushr 32)
            carry = sum ushr 32 != 0L
            arr[index++] = sum.toInt()
        }

        // If there is still a digit to carry add it and return
        if (carry) {
            val t = arr.copyOf(arr.size + 1)
            t[arr.size] = 1
            return t
        }

        // If no more carry just add the remaining digits
        while(index < a.size)
            arr[index] = a[index++]

        return arr
    }

    internal fun subtract(x: IntArray, y: IntArray): IntArray {
        // Input is always expected to have x as the largest array and adjust result sign accordingly
        val arr = IntArray(x.size) // TODO is there a benefit in copying starting array? need to performance test first
        var index = 0
        var diff = 0L

        while(index < y.size) {
            diff = (x[index].toLong() and LONG_MASK) - (y[index].toLong() and LONG_MASK) + (diff shr 32)
            arr[index++] = diff.toInt()
        }

        var borrow = (diff shr 32) != 0L
        while(index < x.size && borrow) {
            diff = (x[index].toLong() and LONG_MASK) - 1
            borrow = diff shr 32 != 0L
            arr[index++] = diff.toInt()
        }

        while(index < x.size)
            arr[index] = x[index++]

        return removeLeadingZeros(arr)
    }

    private fun removeLeadingZeros(arr: IntArray): IntArray {
        var digits = 0
        for(i in arr.size -1 downTo 0) {
            if (arr[i] == 0)
                digits++
            else
                break
        }

        if (digits > 0) // TODO, this drops elements from end of array is there a way to do that without copy?
            return arr.copyOf(arr.size - digits)

        return arr
    }
}