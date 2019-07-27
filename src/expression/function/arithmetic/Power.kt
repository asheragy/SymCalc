package org.cerion.symcalc.expression.function.arithmetic

import expression.constant.I
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr

class Power(vararg e: Expr) : FunctionExpr(Function.POWER, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

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
                val n1 = a as NumberExpr

                // Complex power implemented here since the result is not always a NumberExpr
                if (b.isComplex && !b.asComplex().img.isZero)
                    return complexPower(n1, b.asComplex())

                return n1.power(b)
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

        val result: Expr = Times(pow, Plus(cos, Times(I(), sin)))
        // TODO when functions create other functions the env is not passed, this is a big problem
        if (b.env.isNumericalEval)
            result.env.setNumericalEval(true, b.env.precision)

        val debug = result.eval()
        return result.eval()
    }

    override fun toString(): String {
        return if (size == 2) get(0).toString() + "^" + get(1) else super.toString()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
    }
}
