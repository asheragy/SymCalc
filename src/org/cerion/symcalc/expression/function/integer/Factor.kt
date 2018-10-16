package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum

class Factor(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.FACTOR, *e) {

    override fun evaluate(): Expr {
        var num = get(0).asInteger()

        val list = ListExpr()
        while (num.isEven) {
            num = num.divide(IntegerNum.TWO).asInteger()
            list.add(IntegerNum.TWO)
        }

        // Continue factoring 3+
        if (!num.isOne) {
            var test = IntegerNum(3)

            // TODO add some operator overloading to remove Times() and divide()
            var max = Times(test, test).eval().asInteger()

            while (max.compareTo(num) <= 0) {

                val mod = num.mod(test)
                if (mod.isZero) {
                    list.add(test)
                    num = num.divide(test) as IntegerNum
                } else {
                    test = Plus(test, IntegerNum.TWO).eval().asInteger()
                    max = Times(test, test).eval().asInteger()
                }
            }

            list.add(num)
        }

        return list
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberExpr.INTEGER)
    }
}
