package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.Expr
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

            if (b !is Complex) {
                if (a is Infinity) {
                    return if (b.isNegative)
                        Integer.ZERO
                    else if (b == Rational.HALF) {
                        if(a.direction == -1)
                            I() * Infinity()
                        else
                            Infinity()
                    }
                    else if(b is Integer) {
                        if (b.isOdd && a.direction == -1)
                            Infinity(-1)
                        else
                            Infinity()
                    }
                    else
                        TODO("Unsure how to handle things like -Infinity^1.23")
                }
                if (a is ComplexInfinity)
                    return if (b.isNegative)
                        Integer.ZERO
                    else
                        ComplexInfinity()
            }
        }
        else if (b is Infinity && a is NumberExpr) {
            if (a !is Complex) {
                return if (b.direction == -1)
                    Integer(0)
                else if (a.isNegative)
                    ComplexInfinity()
                else
                    Infinity()
            }
        }

        // Factor out exponents that can be evaluated to non-power form
        if (b is Plus) {
            var powerArgs = b.args.map { Power(a, it).eval() }
            val reducedArgs = powerArgs.filter { it !is Power }
            if (reducedArgs.isNotEmpty()) {
                powerArgs = powerArgs.filterIsInstance<Power>().map { it.args[1] }

                val fullArgs = reducedArgs + Power(a, Plus(*powerArgs.toTypedArray()))
                return Times(*fullArgs.toTypedArray()).eval()
            }
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

        // Spread power to inner terms
        if (a is Times) {
            val args = a.args.map { Power(it, b) }
            return Times(*args.toTypedArray()).eval()
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
        if (args.size == 2) {
            if (args[0] is FunctionExpr)
                return "(" + args[0] + ")^" + args[1]
        }
        return args.toList().joinToString ("^")
    }
}