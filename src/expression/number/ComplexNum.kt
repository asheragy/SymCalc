package org.cerion.symcalc.expression.number

import kotlin.UnsupportedOperationException

class ComplexNum(r: NumberExpr = IntegerNum.ZERO, i: NumberExpr = IntegerNum.ZERO) : NumberExpr() {

    val real: NumberExpr get() = get(0) as NumberExpr
    val img: NumberExpr get() = get(1) as NumberExpr

    override val isZero: Boolean get() = real.isZero && img.isZero
    override val isOne: Boolean get() = real.isOne && img.isZero
    override val numType: NumberType get() = NumberType.COMPLEX

    override val isNegative: Boolean
        get() = throw NotImplementedError() // TODO need InvalidOperation exception

    init {
        setArg(0, r)
        setArg(1, i)
    }

    constructor(r: Int, i: Int) : this(IntegerNum(r.toLong()), IntegerNum(i.toLong()))

    constructor(s: String): this() {
        val num = s.substring(0, s.length - 1)

        if (num.isEmpty())
        //If passed just "i" its complex number 1
            setArg(1, IntegerNum.ONE)
        else
            setArg(1, parse(num))
    }

    fun conjugate(): ComplexNum = ComplexNum(real, img.unaryMinus())

    override fun toString(): String = real.toString() + (if(img.isNegative) "" else "+") + img.toString() + "i"
    override fun unaryMinus(): ComplexNum = ComplexNum(real.unaryMinus(), img.unaryMinus())

    override fun toDouble(): Double {
        throw UnsupportedOperationException("Complex cannot be converted to double")
    }

    override fun evaluate(): NumberExpr {
        if(img.isZero)
            return real

        return this
    }

    /*
    override fun equals(e: NumberExpr): Boolean {
        if (e.isComplex) {
            val c = e as ComplexNum
            return real.equals(c.real) && img.equals(c.img)
        } else if (img.isZero) {
            //Non complex number can be equal as long as the imaginary part is zero
            return real.equals(e)
        }

        return false
    }
    */

    override fun plus(other: NumberExpr): NumberExpr {
        if (other.numType === NumberType.COMPLEX) {
            val b = other.asComplex()
            return ComplexNum(real + b.real, img + b.img).evaluate()
        }

        return ComplexNum(real + other, img).evaluate()
    }

    override fun minus(other: NumberExpr): NumberExpr {
        val resultR: NumberExpr
        val resultI: NumberExpr

        if (other.numType === NumberType.COMPLEX) {
            val b = other as ComplexNum
            resultR = real - b.real
            resultI = img - b.img

        } else {
            resultR = real.minus(other)
            resultI = img
        }

        return ComplexNum(resultR, resultI).evaluate()
    }

    override fun times(other: NumberExpr): NumberExpr {
        val resultR: NumberExpr
        val resultI: NumberExpr

        when (other.numType) {
            NumberType.COMPLEX -> {
                //(a+bi)(c+di) = (ac-bd) + (bc+ad)i
                val a = this.real
                val b = this.img
                val c = other.asComplex().real
                val d = other.asComplex().img

                resultR = (a * c) - (b * d)
                resultI = (b * c) + (a * d)
            }
            else -> {
                resultR = real * other
                resultI = img * other
            }
        }

        return ComplexNum(resultR, resultI).evaluate()
    }

    override fun div(other: NumberExpr): NumberExpr {
        val resultR: NumberExpr
        val resultI: NumberExpr

        when(other.numType) {
            NumberType.COMPLEX -> {

                val conj = other.asComplex().conjugate()
                var top = (this * conj)
                if (top !is ComplexNum)
                    top = ComplexNum(top)

                val bottom = other.asComplex() * conj // This should not be a complex number

                if (top.real.isInteger && bottom.isInteger)
                    resultR = RationalNum(top.real.asInteger(), bottom.asInteger()).evaluate()
                else
                    resultR = top.real / bottom

                if (top.img.isInteger && bottom.isInteger)
                    resultI = RationalNum(top.img.asInteger(), bottom.asInteger())
                else
                    resultI = top.img / bottom
            }
            else -> {
                resultR = real / other
                resultI = img / other
            }
        }

        return ComplexNum(resultR, resultI).evaluate()
    }

    override fun power(other: NumberExpr): NumberExpr {
        // TODO This fixes the single use case
        if (other.isOne)
            return this

        if (other.isZero)
            return ComplexNum(1, 0)


        when (other.numType) {
            NumberType.INTEGER -> TODO()
            NumberType.RATIONAL -> TODO()
            NumberType.REAL -> TODO()
            NumberType.COMPLEX -> {
                throw UnsupportedOperationException()
            }
        }
    }

    override fun compareTo(other: NumberExpr): Int {
        if(other.isComplex) {
            val o = other.asComplex()
            if (real == o.real && img == o.img)
                return 0

            // Can't compare so they are either equal or not able to determine
        }
        else if(img.isZero) {
            return real.compareTo(other)
        }

        throw UnsupportedOperationException("Unable to compare complex")
    }

    companion object {
        @JvmField val ZERO = ComplexNum()
    }
}