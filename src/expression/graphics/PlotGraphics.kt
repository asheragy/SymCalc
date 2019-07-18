package org.cerion.symcalc.expression.graphics

import org.cerion.symcalc.UserFunction
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.GraphicsExpr
import org.cerion.symcalc.expression.number.IntegerNum

class PlotGraphics(private val function: UserFunction, private val min: IntegerNum, private val max: IntegerNum) : GraphicsExpr() {

    override fun treeForm(i: Int) {

        for (x in min.intValue()..max.intValue()) {
            val e = function.eval(IntegerNum(x))
            println(x.toString() + "\t" + e)
        }


    }
}
