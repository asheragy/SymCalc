package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import expression.function.trig.ArcTan
import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.*
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow

class Power(vararg e: Expr) : FunctionExpr(*e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        // Power to power is just multiplied exponents
        if (a is Power) {
            return Power(a[0], Times(a[1], b)).eval()
        }

        if (a is NumberExpr && a.isOne)
            return a

        if (b is NumberExpr) {
            // Zero/Identity is just a shortcut for special case, unit tests should still pass if this is commented out
            if (b.isZero)
                return Integer.ONE
            if (b.isOne)
                return a
        }

        if (b is Plus) {
            return Times(*b.args.map { Power(a, it) }.toTypedArray()).eval()
        }

        // Euler's Identity
        if (a is E && b is Times && b.size == 2 && b[0] is Complex) {
            val img = Times((b[0] as Complex).img, b[1])
            return complexPower(a, Integer.ZERO, img)
        }

        if (a is NumberExpr && b is NumberExpr) {
            if (a is Integer && b is Rational)
                return a.pow(b)
            else if (a is Rational && b is Rational) {
                return Divide(Power(a.numerator, b), Power(a.denominator, b)).eval()
            }

            // Complex power implemented here since the result is not always a NumberExpr
            if (b is Complex && !b.img.isZero) {
                if (a is Complex)
                    return complexToPower(a, b)

                return complexPower(a, b)
            }

            when (a) {
                is Integer -> return a.power(b)
                is Rational -> return a.power(b)
                is RealDouble -> return a.power(b)
                is RealBigDec -> return a.power(b)
                is Complex -> return complexToPower(a, b)
            }
        }

        return this
    }

    private fun complexToPower(z: Complex, N: NumberExpr): Expr {
        if (N is Integer) {
            return z.pow(N.intValue())
        }

        if (N is Complex)
            return z.pow(N)

        var a = z
        if (N.precision < z.precision)
            a = z.toPrecision(N.precision) as Complex

        if (a.precision == InfinitePrecision)
            return this

        val theta = ArcTan(Divide(a.img, a.real)).eval()
        val r = Sqrt(Plus(a.real.square(), a.img.square())).eval()
        val rN = Power(r, N).eval()
        val cos = Cos(Times(theta, N)).eval()
        val sin = Sin(Times(theta, N)).eval()

        val real = Times(rN, cos)
        val img = Times(rN, sin)

        return Plus(real, Times(img,I())).eval()
    }

    private fun complexPower(a: Expr, n: Complex): Expr {
        return complexPower(a, n.real, n.img)
    }

    // a^(b + ic)
    private fun complexPower(a: Expr, b: Expr, c: Expr): Expr {
        if (a is Complex)
            throw OperationException("Function only supports non-complex to complex power")

        val clog = Times(c, Log(a))
        val cos = Cos(clog)
        val sin = Sin(clog)
        val pow = Power(a, b)

        val result = Times(pow, Plus(cos, Times(I(), sin)))
        val e = result.eval()

        // If not evaluated fully return the original expression
        if (e is NumberExpr)
            return e

        return this
    }

    override fun toString(): String {
        return if (size == 2) get(0).toString() + "^" + get(1) else super.toString()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
    }
}

private fun Integer.power(other: NumberExpr): NumberExpr {
    return when (other.numType) {
        NumberType.INTEGER -> this.pow(other as Integer)
        NumberType.RATIONAL -> throw UnsupportedOperationException()
        NumberType.REAL_DOUBLE -> {
            val real = this.toPrecision(other.precision) as RealDouble
            real.power(other)
        }
        NumberType.REAL_BIGDEC -> {
            val real = this.toPrecision(other.precision) as RealBigDec
            real.power(other)
        }
        NumberType.COMPLEX -> throw UnsupportedOperationException()
    }
}

private fun Rational.power(other: NumberExpr): NumberExpr {
    when (other.numType) {
        NumberType.INTEGER -> {
            val top = numerator.power(other) // These could be rational on negative integer
            val bottom = denominator.power(other)
            return Divide(top, bottom).eval() as NumberExpr
        }
        NumberType.REAL_DOUBLE -> {
            val real = this.toPrecision(other.precision) as RealDouble
            return real.power(other)
        }
        NumberType.REAL_BIGDEC -> {
            val real = this.toPrecision(other.precision) as RealBigDec
            return real.power(other)
        }
        NumberType.RATIONAL -> throw UnsupportedOperationException()
        NumberType.COMPLEX -> throw UnsupportedOperationException()
    }
}

private fun RealDouble.power(other: NumberExpr): NumberExpr {
    when (other) {
        is Integer,
        is Rational -> return this.power(other.toPrecision(precision))
        is RealDouble -> {
            val pow = value.pow(other.value)
            if (pow.isNaN() && value < 0)
                return Exp(Times(other, Plus(Log(this.unaryMinus()), Times(I(), Pi())))).eval() as NumberExpr

            return RealDouble(pow)
        }
        is RealBigDec -> return RealDouble(value.pow(other.toDouble()))
        is Complex -> return Power(this, other).eval() as NumberExpr
        else -> throw NotImplementedError()
    }
}

private fun RealBigDec.power(other: NumberExpr): NumberExpr {
    when (other) {
        is Integer -> {
            val number = value.pow(other.intValue(), MathContext(precision, RoundingMode.HALF_UP))
            return RealBigDec(number)
        }
        is Rational -> return this.power(other.toPrecision(precision))
        is RealDouble -> return RealDouble(toDouble().pow(other.value))
        is RealBigDec -> {
            if (this.isNegative)
                return Exp(Times(other, Plus(Log(this.unaryMinus()), Times(I(), Pi())))).eval() as NumberExpr

            return this.pow(other)
        }
        is Complex -> throw UnsupportedOperationException()
        else -> throw NotImplementedError()
    }
}