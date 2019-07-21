package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr

class Power(vararg e: Expr) : FunctionExpr(Function.POWER, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        if (a.isNumber && b.isNumber) {
            val n1 = a as NumberExpr
            val n2 = b as NumberExpr

            // TODO Zero/Identity is just a shortcut for special case, unit tests should still pass if this is commented out

            // Zero
            if (n2.isZero)
                return IntegerNum.ONE

            //Identity
            if (n2.isOne)
                return a

            return n1.power(n2)
        }

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
