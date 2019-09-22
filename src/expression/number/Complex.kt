package org.cerion.symcalc.expression.number

import expression.constant.I
import expression.function.trig.ArcTan
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import kotlin.math.abs
import kotlin.math.min

class Complex(val real: NumberExpr, val img: NumberExpr) : NumberExpr() {
    companion object {
        @JvmField val ZERO = Complex(Integer.ZERO, Integer.ZERO)
        @JvmField val I = Complex(0, 1)
    }

    override val type: ExprType get() = ExprType.NUMBER
    override val isZero: Boolean get() = real.isZero && img.isZero
    override val isOne: Boolean get() = real.isOne && img.isZero
    override val numType: NumberType get() = NumberType.COMPLEX
    override val precision: Int get() = min(real.precision, img.precision)

    override val isNegative: Boolean
        get() = throw UnsupportedOperationException()

    constructor(r: NumberExpr) : this(r, Integer.ZERO)
    constructor(r: Number, i: Number) : this(create(r), create(i))
    constructor(r: String, i: String): this(RealBigDec(r), RealBigDec(i))

    fun conjugate(): Complex = Complex(real, img.unaryMinus())

    //override fun toString(): String = real.toString() + (if(img.isNegative) "" else "+") + img.toString() + "i"
    override fun toString(): String = "Complex[$real, $img]"
    override fun unaryMinus(): Complex = Complex(real.unaryMinus(), img.unaryMinus())

    override fun eval(): NumberExpr {
        if(img.isZero)
            return real

        return this
    }

    override fun toPrecision(precision: Int): NumberExpr {
        return Complex(real.toPrecision(precision), img.toPrecision(precision))
    }

    override fun plus(other: NumberExpr): NumberExpr {
        if (other.numType === NumberType.COMPLEX) {
            other as Complex
            return Complex(real + other.real, img + other.img).eval()
        }

        return Complex(real + other, img).eval()
    }

    override fun times(other: NumberExpr): NumberExpr {
        val resultR: NumberExpr
        val resultI: NumberExpr

        when (other.numType) {
            NumberType.COMPLEX -> {
                other as Complex
                //(a+bi)(c+di) = (ac-bd) + (bc+ad)i
                val a = this.real
                val b = this.img
                val c = other.real
                val d = other.img

                resultR = (a * c) - (b * d)
                resultI = (b * c) + (a * d)
            }
            else -> {
                resultR = real * other
                resultI = img * other
            }
        }

        return Complex(resultR, resultI).eval()
    }

    override fun div(other: NumberExpr): NumberExpr {
        val resultR: NumberExpr
        val resultI: NumberExpr

        when(other.numType) {
            NumberType.COMPLEX -> {
                other as Complex
                val conj = other.conjugate()
                var top = (this * conj)
                if (top !is Complex)
                    top = Complex(top)

                val bottom = other * conj // This should not be a complex number

                resultR = top.real / bottom
                resultI = top.img / bottom
            }
            else -> {
                resultR = real / other
                resultI = img / other
            }
        }

        return Complex(resultR, resultI).eval()
    }

    fun pow(n: Int): NumberExpr {
        var total: NumberExpr = this
        var result: NumberExpr = Integer.ONE
        var i = abs(n)
        while(i > 0) {
            if (i % 2 == 1)
                result *= total

            total *= total
            i /= 2
        }

        if (n < 0)
            return Integer.ONE / result

        return result
    }

    infix fun pow(n: Integer): NumberExpr {
        return this.pow(n.intValue())
    }

    fun pow(z: Complex): Expr {
        // http://mathworld.wolfram.com/ComplexExponentiation.html
        val a = real
        val b = img

        val theta = ArcTan(a / b).eval()
        val a2b2 = a.square() + b.square()
        val exp1 = Power(a2b2, z / Integer.TWO)
        val exp2 = Power(E(), Times(I(), z, theta))

        val result = exp1 * exp2
        if (result is NumberExpr)
            return result

        return Power(this, z)
    }

    override fun pow(other: NumberExpr): NumberExpr {
        if (other is Complex)
            throw UnsupportedOperationException()

        val z = this
        var a = this
        val N = other
        if (N.precision < z.precision)
            a = z.toPrecision(N.precision) as Complex

        val theta = ArcTan(a.img / a.real)
        val r = Sqrt(a.real.square() + a.img.square())
        val rN = Power(r, N)
        val cos = Cos(Times(theta, N))
        val sin = Sin(Times(theta, N))

        val real = Times(rN, cos)
        val img = Times(rN, sin)

        return (real + Times(img,I())) as NumberExpr
    }

    override fun compareTo(other: NumberExpr): Int {
        if(other is Complex) {
            if (img.isZero && other.img.isZero)
                return real.compareTo(other.real)

            if (real == other.real && img == other.img)
                return 0

            // Can't compare so they are either equal or not able to determine
        }
        else if(img.isZero) {
            return real.compareTo(other)
        }

        throw UnsupportedOperationException("Unable to compare complex")
    }
}