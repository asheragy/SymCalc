package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class Factor(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        var num = get(0) as Integer

        val listArgs = mutableListOf<Expr>()
        while (num.isEven) {
            num = (num / Integer.TWO).asInteger()
            listArgs.add(Integer.TWO)
        }

        // Continue factoring 3+
        if (!num.isOne) {
            var test = Integer(3)
            var max = test * test

            while (max <= num) {

                val mod = num.rem(test)
                if (mod.isZero) {
                    listArgs.add(test)
                    num = (num / test).asInteger()
                } else {
                    test+= Integer.TWO
                    max = test * test
                }
            }

            listArgs.add(num)
        }

        return ListExpr(listArgs)
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}
