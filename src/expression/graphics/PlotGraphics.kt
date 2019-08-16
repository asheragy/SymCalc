package org.cerion.symcalc.expression.graphics

import org.cerion.symcalc.UserFunction
import org.cerion.symcalc.expression.GraphicsExpr
import org.cerion.symcalc.expression.number.Integer

class PlotGraphics(private val function: UserFunction, private val min: Integer, private val max: Integer) : GraphicsExpr() {

    override fun treeForm(i: Int) {

        for (x in min.intValue()..max.intValue()) {
            val e = function.eval(Integer(x))
            println(x.toString() + "\t" + e)
        }


    }
}
