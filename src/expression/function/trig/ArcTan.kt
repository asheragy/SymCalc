package expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.trig.TrigBase
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath

class ArcTan(vararg e: Expr) : TrigBase(Function.ARCTAN, *e) {

    override fun evaluateAsBigDecimal(n: RealBigDec): RealBigDec = RealBigDec(BigDecimalMath.atan(n.value))
    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        if (e is Integer && e.isOne) {
            return Divide(Pi(), Integer(4))
        }

        return this
    }
}