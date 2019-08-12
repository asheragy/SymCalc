package expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.trig.TrigBase
import org.cerion.symcalc.expression.number.IntegerNum

class ArcTan(vararg e: Expr) : TrigBase(Function.ARCTAN, *e) {

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        if (e is IntegerNum && e.isOne) {
            return Divide(Pi(), IntegerNum(4))
        }

        return this
    }

}