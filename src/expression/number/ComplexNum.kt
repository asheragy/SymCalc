package org.cerion.symcalc.expression.number

import expression.constant.I
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin

class ComplexNum(r: NumberExpr = IntegerNum.ZERO, i: NumberExpr = IntegerNum.ZERO) : NumberExpr() {

    var real: NumberExpr
        get() = get(0) as NumberExpr
        private set(n) = setArg(0, n)

    var img: NumberExpr
        get() = get(1) as NumberExpr
        private set(n) = setArg(1, n)

    override val isZero: Boolean get() = real.isZero && img.isZero
    override val isOne: Boolean get() = real.isOne && img.isZero
    override val numType: NumberType get() = NumberType.COMPLEX

    override val isNegative: Boolean
        get() = throw NotImplementedError() // TODO need InvalidOperation exception

    init {
        real = r
        img = i
    }

    constructor(r: Int, i: Int) : this(IntegerNum(r.toLong()), IntegerNum(i.toLong()))

    constructor(s: String) : this() {
        val num = s.substring(0, s.length - 1)

        if (num.isEmpty())
        //If passed just "i" its complex number 1
            img = IntegerNum.ONE
        else
            img = parse(num)
    }

    fun conjugate(): ComplexNum = ComplexNum(real, img.unaryMinus())

    override fun toString(): String = real.toString() + (if(img.isNegative) "" else "+") + img.toString() + "i"
    override fun unaryMinus(): ComplexNum = ComplexNum(real.unaryMinus(), img.unaryMinus())

    override fun toDouble(): Double {
        println("Error ComplexNum.toDouble()")
        return 0.0
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
            return ComplexNum(real + b.real, img + b.img)
        }

        return ComplexNum(real + other, img)
    }

    override fun minus(other: NumberExpr): NumberExpr {
        val result = ComplexNum()
        if (other.numType === NumberType.COMPLEX) {
            val b = other as ComplexNum
            result.real = real - b.real
            result.img = img - b.img

        } else {
            result.real = real.minus(other)
            result.img = img
        }

        return result
    }

    override fun times(other: NumberExpr): NumberExpr {
        val result = ComplexNum()
        when (other.numType) {
            NumberType.COMPLEX -> {
                //(a+bi)(c+di) = (ac-bd) + (bc+ad)i
                val a = this.real
                val b = this.img
                val c = other.asComplex().real
                val d = other.asComplex().img

                result.real = (a * c) - (b * d)
                result.img = (b * c) + (a * d)
            }
            else -> {
                result.real = real * other
                result.img = img * other
            }
        }

        return result
    }

    override fun div(other: NumberExpr): NumberExpr {
        val result = ComplexNum()

        when(other.numType) {
            NumberType.COMPLEX -> {
                val conj = other.asComplex().conjugate()
                val top = (this * conj).asComplex()
                val bottom = (other.asComplex() * conj).asComplex()

                if (top.real.isInteger && bottom.real.isInteger)
                    result.real = RationalNum(top.real.asInteger(), bottom.real.asInteger())
                else
                    result.real = top.real / bottom.real

                if (top.img.isInteger && bottom.real.isInteger)
                    result.img = RationalNum(top.img.asInteger(), bottom.real.asInteger())
                else
                    result.img = top.img / bottom.real
            }
            else -> {
                result.real = real / other
                result.img = img / other
            }
        }

        return result
    }

    override fun canExp(other: NumberExpr): Boolean {
        return false
    }

    override fun power(other: NumberExpr): Expr {
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
                if (img.isZero) {
                    val a = real
                    val b = other.asComplex().real
                    val c = other.asComplex().img

                    val clog = Times(c, Log(a))
                    val cos = Cos(clog)
                    val sin = Sin(clog)
                    val pow = Power(a, b)

                    val result: Expr = Times(pow, Plus(cos, Times(I(), sin)))
                    // TODO when functions create other functions the env is not passed, this is a big problem
                    if (other.env.isNumericalEval)
                        result.env.setNumericalEval(true, other.env.precision)

                    return result.eval()
                }

                TODO()
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

        // TODO this may be implemented but some cases will throw exception
        throw NotImplementedError()
    }

    companion object {
        @JvmField val ZERO = ComplexNum()
    }
}