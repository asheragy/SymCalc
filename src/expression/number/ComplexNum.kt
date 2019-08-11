package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Divide
import kotlin.UnsupportedOperationException
import kotlin.math.abs
import kotlin.math.min

class ComplexNum(r: NumberExpr = IntegerNum.ZERO, i: NumberExpr = IntegerNum.ZERO) : NumberExpr() {

    override val value: Any? get() = null

    val real: NumberExpr get() = get(0) as NumberExpr
    val img: NumberExpr get() = get(1) as NumberExpr

    override val isZero: Boolean get() = real.isZero && img.isZero
    override val isOne: Boolean get() = real.isOne && img.isZero
    override val numType: NumberType get() = NumberType.COMPLEX
    override val precision: Int get() = min(real.precision, img.precision)

    override val isNegative: Boolean
        get() = throw UnsupportedOperationException()

    init {
        setArg(0, r)
        setArg(1, i)
    }

    constructor(r: Int, i: Int) : this(IntegerNum(r.toLong()), IntegerNum(i.toLong()))
    constructor(r: Double, i: Double) : this(RealNum.create(r), RealNum.create(i))

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
        if (img.isZero)
            return real.toDouble()

        throw UnsupportedOperationException("Complex cannot be converted to double")
    }

    override fun evaluate(): NumberExpr {
        if(img.isZero)
            return real

        return this
    }

    override fun evaluate(precision: Int): NumberExpr {
        return ComplexNum(real.evaluate(precision), img.evaluate(precision))
    }


    override fun plus(other: NumberExpr): NumberExpr {
        if (other.numType === NumberType.COMPLEX) {
            val b = other.asComplex()
            return ComplexNum(real + b.real, img + b.img).evaluate()
        }

        return ComplexNum(real + other, img).evaluate()
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
                    resultR = Rational(top.real.asInteger(), bottom.asInteger()).evaluate()
                else
                    resultR = top.real / bottom

                if (top.img.isInteger && bottom.isInteger)
                    resultI = Rational(top.img.asInteger(), bottom.asInteger())
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

    fun pow(n: Int): NumberExpr {
        var total: NumberExpr = this
        var result: NumberExpr = IntegerNum.ONE
        var i = abs(n)
        while(i > 0) {
            if (i % 2 == 1)
                result *= total

            total = total * total

            i /= 2
        }

        if (n < 0)
            return IntegerNum.ONE / result

        return result
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