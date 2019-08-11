package expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.trig.TrigBase
import org.cerion.symcalc.expression.number.*
import org.cerion.symcalc.expression.number.RealNum_Double
import java.lang.Math.atan

class ArcTan(vararg e: Expr) : TrigBase(Function.ARCTAN, *e) {

    override fun evaluateAsDouble(d: Double): Expr {
        return RealNum_Double(kotlin.math.atan(d))
    }

    override fun evaluate(e: Expr): Expr {
        if (e is IntegerNum && e.isOne) {
            return Divide(Pi(), IntegerNum(4))
        }

        TODO("not implemented: $this") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Can't really do anything with this, maybe have this function as a subset of TrigBase since it does not apply to inverse functions
        return evaluate(Times(Pi(), e))
    }

}