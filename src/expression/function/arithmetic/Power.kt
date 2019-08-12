package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import expression.function.trig.ArcTan
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.integer.Factor
import org.cerion.symcalc.expression.function.list.Tally
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.*
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow

class Power(vararg e: Expr) : FunctionExpr(Function.POWER, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        // Power to power is just multiplied exponents
        if (a is Power) {
            return Power(a[0], Times(a[1], b)).eval()
        }

        if (a is NumberExpr && a.isOne)
            return a

        if (b.isNumber) {
            b as NumberExpr
            // Zero/Identity is just a shortcut for special case, unit tests should still pass if this is commented out
            if (b.isZero)
                return IntegerNum.ONE
            if (b.isOne)
                return a
        }

        if (b is Plus) {
            val result = Times()
            b.args.forEach { result.add(Power(a, it)) }

            return result.eval()
        }

        // Euler's Identity
        if (a is E && b is Times && b.size == 2 && b[0] is Complex) {
            val img = Times(b[0].asNumber().asComplex().img, b[1])
            return complexPower(a, IntegerNum.ZERO, img)
        }

        if (b.isNumber) {
            b as NumberExpr
            if (a.isNumber) {
                a as NumberExpr

                if (a.isInteger && b.isRational)
                    return integerToRational(a.asInteger(), b.asRational())
                else if (a.isRational && b.isRational) {
                    a as Rational
                    return Divide(Power(a.numerator, b), Power(a.denominator, b)).eval()
                }

                // Complex power implemented here since the result is not always a NumberExpr
                if (b.isComplex && !b.asComplex().img.isZero)
                    return complexPower(a, b.asComplex())

                if (a is IntegerNum)
                    return a.power(b)
                else if (a is Rational)
                    return a.power(b)
                else if (a is RealNum)
                    return a.power(b)
                else if (a is Complex)
                    return complexToPower(a, b)
            }

            // TODO this is not the right check, just gets past the current issue
            if (a.isConst && b is Complex)
                return complexPower(a, b)
        }

        return this
    }

    private fun complexToPower(z: Complex, N: NumberExpr): Expr {
        if (N is IntegerNum) {
            return z.pow(N.intValue())
        }

        var a = z
        if (N.precision < z.precision)
            a = z.evaluate(N.precision) as Complex

        if (a.precision == InfinitePrecision)
            return this

        val theta = ArcTan(Divide(a.img, a.real)).eval()
        val r = Sqrt(Plus(a.real.square(), a.img.square())).eval()
        val rN = Power(r, N).eval()
        val cos = Cos(Times(theta, N)).eval()
        val sin = Sin(Times(theta, N)).eval()

        return Times(rN, Plus(cos, Times(I(), sin))).eval()
    }

    private fun complexPower(a: Expr, n: Complex): Expr {
        return complexPower(a, n.real, n.img)
    }

    private fun complexPower(a: Expr, real: Expr, img: Expr): Expr {
        if (a.isNumber && a.asNumber().isComplex && !a.asNumber().asComplex().img.isZero)
            TODO("This only handles a^(b + ci)")

        val b = real
        val c = img

        val clog = Times(c, Log(a))
        val cos = Cos(clog)
        val sin = Sin(clog)
        val pow = Power(a, b)

        val result = Times(pow, Plus(cos, Times(I(), sin)))
        val e = result.eval()

        // If not evaluated fully return the original expression
        if (e is Times)
            return this

        return e
    }

    private fun integerToRational(a: IntegerNum, b: Rational): Expr {
        // Performance: Use faster method for square root
        /* One other method for any value but may need additional checks
             - Calculate using Math.pow() after converting to doubles
             - isWholeNumber = floor(value) && !java.lang.Double.isInfinite(value)
             - Then convert to integer and return
        */
        if (b == Rational.HALF) {
            val sqrt = a.value.sqrtAndRemainder()
            if (sqrt[1].compareTo(BigInteger.ZERO) == 0)
                return IntegerNum(sqrt[0])
        }

        // factor out any numbers that are the Nth root of the denominator
        val t = Factor(a).eval()
        val factors = Tally(t).eval().asList()

        val denominator = b.denominator
        var multiply = IntegerNum.ONE

        var i = 0
        while (i < factors.size) {
            val key = factors[i][0].asInteger()
            val v = factors[i][1].asInteger()

            // Factor it out
            if (v >= denominator) {
                multiply *= key
                factors[i] = ListExpr(key, v - denominator)
            } else
                i++
        }

        if (multiply.isOne)
            return Power(a, b)

        // Factor out multiples
        //Expr result = new Power(this, num);
        var root = IntegerNum.ONE
        for (j in 0 until factors.size) {
            val f1 = factors[j][0]
            val f2 = factors[j][1] as IntegerNum
            if (f2.isZero)
                continue

            root = Times(root, f1, f2).eval().asInteger()
        }

        val nthRoot = Times(multiply, Power(root, b))
        return Power(nthRoot, b.numerator).eval()

    }

    override fun toString(): String {
        return if (size == 2) get(0).toString() + "^" + get(1) else super.toString()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
    }
}

private fun IntegerNum.power(other: NumberExpr): NumberExpr {
    when (other.numType) {
        NumberType.INTEGER -> return this.pow(other as IntegerNum)
        NumberType.RATIONAL -> throw UnsupportedOperationException()
        NumberType.REAL -> {
            val real = this.evaluate(other.precision) as RealNum
            return real.power(other)
        }
        NumberType.COMPLEX -> {
            throw UnsupportedOperationException()
            /*
            val complex = other.asComplex()
            if (complex.img.isZero)
                return this.power(complex.real)

            return Complex(this).power(other)

             */
        }
    }
}

private fun Rational.power(other: NumberExpr): NumberExpr {
    val n: IntegerNum
    val d: IntegerNum
    when (other.numType) {
        NumberType.INTEGER -> {
            val top = numerator.power(other) // These could be rational on negative integer
            val bottom = denominator.power(other)
            return Divide(top, bottom).eval() as NumberExpr
        }

        NumberType.REAL -> {
            val real = this.evaluate(other.precision) as RealNum
            return real.power(other)
        }
        NumberType.RATIONAL -> throw UnsupportedOperationException()

        NumberType.COMPLEX -> {
            throw UnsupportedOperationException()
            /*
            other as Complex
            if (other.img.isZero)
                return this.power(other.real)

            return Complex(this).power(other)

             */
        }

    }
}

private fun RealNum.power(other: NumberExpr): NumberExpr {
    if (this is RealNum_BigDecimal)
        return this.power(other)

    this as RealNum_Double
    return this.power(other)
}

private fun RealNum_Double.power(other: NumberExpr): NumberExpr {
    when (other.numType) {
        NumberType.INTEGER,
        NumberType.RATIONAL -> return this.power(other.evaluate(precision))
        NumberType.REAL -> return RealNum_Double(value.pow(other.toDouble()))
        NumberType.COMPLEX ->
            return Power(this, other).eval() as NumberExpr
    }
}

private fun RealNum_BigDecimal.power(other: NumberExpr): NumberExpr {
    // Special case square root
    if(other.isRational && other.equals(Rational(1,2)))
        return RealNum_BigDecimal(value.sqrt(MathContext(precision, RoundingMode.HALF_UP)))

    when (other.numType) {
        NumberType.INTEGER -> {
            val number = value.pow(other.asInteger().intValue(), MathContext(precision, RoundingMode.HALF_UP))
            return RealNum_BigDecimal(number)
        }
        NumberType.RATIONAL -> {
            return this.power(other.evaluate(precision))
        }
        NumberType.REAL -> {
            other as RealNum
            if (other is RealNum_Double)
                return RealNum_Double(toDouble().pow(other.value))

            return this.pow(other as RealNum_BigDecimal)
        }

        NumberType.COMPLEX -> {
            throw UnsupportedOperationException()
            //return Complex(this).power(other)
        }
    }
}

/*
private fun Complex.power(other: NumberExpr): NumberExpr {
    when (other.numType) {
        NumberType.INTEGER,
        NumberType.RATIONAL,
        NumberType.REAL,
        NumberType.COMPLEX ->

    }
}

 */