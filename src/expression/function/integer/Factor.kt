package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

class Factor(vararg e: Expr) : FunctionExpr(Function.FACTOR, *e) {

    override fun evaluate(): Expr {
        var num = get(0) as Integer

        val list = ListExpr()
        while (num.isEven) {
            num = (num / Integer.TWO).asInteger()
            list.add(Integer.TWO)
        }

        // Continue factoring 3+
        if (!num.isOne) {
            var test = Integer(3)
            var max = test * test

            while (max <= num) {

                val mod = num.rem(test)
                if (mod.isZero) {
                    list.add(test)
                    num = (num / test).asInteger()
                } else {
                    test+= Integer.TWO
                    max = test * test
                }
            }

            list.add(num)
        }

        return list
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}
