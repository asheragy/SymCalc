package org.cerion.symcalc.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.NumberType

class PowerMod(vararg e: Expr) : FunctionExpr(*e) {

    public override fun evaluate(): Expr {
        val a = get(0).asInteger()
        val b = get(1).asInteger()
        val c = get(2).asInteger()
        return a.powerMod(b, c)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateNumberType(0, NumberType.INTEGER)
        validateNumberType(1, NumberType.INTEGER)
        validateNumberType(2, NumberType.INTEGER)
    }
}
