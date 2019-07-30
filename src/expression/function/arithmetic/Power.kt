package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.integer.Factor
import org.cerion.symcalc.expression.function.list.Tally
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.*
import kotlin.math.min

class Power(vararg e: Expr) : FunctionExpr(Function.POWER, *e) {

    public override fun evaluate(): Expr {
        var a = get(0)
        var b = get(1)

        // TODO this should be done in Expr class, may only apply to ConstExpr so that is a factor
        val minPrecision = min(a.precision, b.precision)
        if (a is ConstExpr && minPrecision < a.precision)
            a = N(a, minPrecision).eval()
        if ((b is ConstExpr || b is Times) && minPrecision < b.precision)
            b = N(b, minPrecision).eval()

        if (b.isNumber) {
            b as NumberExpr

            if(a.isNumber || a.isConst) {
                // Zero/Identity is just a shortcut for special case, unit tests should still pass if this is commented out

                // Zero
                if (b.isZero)
                    return IntegerNum.ONE

                //Identity
                if (b.isOne)
                    return a
            }

            if (a.isNumber) {
                a as NumberExpr

                if (a.isInteger && b.isRational)
                    return integerToRational(a.asInteger(), b.asRational())

                // Complex power implemented here since the result is not always a NumberExpr
                if (b.isComplex && !b.asComplex().img.isZero)
                    return complexPower(a, b.asComplex())

                return a.power(b)
            }

            // TODO this is not the right check, just gets past the current issue
            if (a.isConst && b is ComplexNum)
                return complexPower(a, b)
        }

        if (b is Plus) {
            val result = Times()
            b.args.forEach { result.add(Power(a, it)) }

            return result.eval()
        }

        // Euler's Identity
        if (a is E && b is Times && b.size == 2 && b[0] is ComplexNum) {
            val img = Times(b[0].asNumber().asComplex().img, b[1])
            return complexPower(a, IntegerNum.ZERO, img)
        }

        return this
    }

    private fun complexPower(a: Expr, n: ComplexNum): Expr {
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

        return Times(pow, Plus(cos, Times(I(), sin))).eval()
    }

    private fun integerToRational(a: IntegerNum, b: RationalNum): Expr {

        val pow = Math.pow(a.toDouble(), b.toDouble())
        val real = RealNum.create(pow)

        if (!isNumericalEval) {
            if (real.isWholeNumber)
                return real.toInteger()
            else {
                // factor out any numbers that are the Nth root of the denominator
                val t = Factor(a)
                val factors = Tally(t).eval().asList()
                val denominator = b.denominator

                var multiply = IntegerNum.ONE

                run {
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
                }

                if (multiply.isOne)
                    return Power(a, b)

                // Factor out multiples
                //Expr result = new Power(this, num);
                var root = IntegerNum.ONE
                for (i in 0 until factors.size) {
                    root = Times(root, factors[i][0], factors[i][1]).eval().asInteger()
                }

                return Times(multiply, Power(root, b))
            }
        }

        return real
    }

    override fun toString(): String {
        return if (size == 2) get(0).toString() + "^" + get(1) else super.toString()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
    }
}
