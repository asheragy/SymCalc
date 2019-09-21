package expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.trig.TrigBase
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath

class ArcTan(vararg e: Expr) : TrigBase(*e) {

    // FEAT added minimum to get complex power working

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        val t = x.forcePrecision(RealBigDec.getStoredPrecision(x.precision))
        return RealBigDec(BigDecimalMath.atan(t), x.precision)
    }

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        if (e is Integer && e.isOne) {
            return Divide(Pi(), Integer(4))
        }

        return this
    }
}