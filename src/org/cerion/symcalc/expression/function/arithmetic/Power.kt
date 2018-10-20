package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

class Power(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.POWER, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        // TODO unit test and move to NumberExpr classes
        if (b.isNumber && b.asNumber().isOne)
            return a

        if (a.isNumber && b.isNumber) {
            val n1 = a as NumberExpr
            val n2 = b as NumberExpr

            return n1.power(n2)
        }

        return this
    }

    override fun toString(): String {
        return if (size() == 2) get(0).toString() + "^" + get(1) else super.toString()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
    }
}
