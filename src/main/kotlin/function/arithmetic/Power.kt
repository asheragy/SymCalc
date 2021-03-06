package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.Rational

class Power(vararg e: Any) : FunctionExpr(*e) {

    public override fun evaluate(): Expr {
        if (size == 1)
            return get(0)

        if (size > 2)
            return Power(get(0), Power(*args.sliceArray(1 until size))).eval()

        val a = get(0)
        val b = get(1)

        // Power to power is just multiplied exponents
        if (a is Power)
            return Power(a[0], a[1] * b).eval()

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

            if (a is Complex && b is Complex)
                return a.pow(b)

            // Complex power implemented here since the result is not always a NumberExpr
            if (b is Complex)
                return complexPower(a, b.real, b.img)

            if (a is Complex) {
                if (b is Integer)
                    return a.pow(b.intValue())

                if (a.precision == InfinitePrecision && b.precision == InfinitePrecision)
                    return this
            }

            return a.pow(b)
        }

        return this
    }

    // a^(b + ic)
    private fun complexPower(a: Expr, b: Expr, c: Expr): Expr {
        if (a is Complex)
            throw OperationException("Function only supports non-complex to complex power")

        val clog = Times(c, Log(a))
        val cos = Cos(clog)
        val sin = Sin(clog)
        val pow = Power(a, b)

        val e = pow * (cos + Times(I(), sin))

        // If not evaluated fully return the original expression
        if (e is NumberExpr)
            return e

        return this
    }

    override fun toString(): String {
        return args.toList().joinToString ("^")
    }
}