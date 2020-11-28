package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath

class ArcTan(e: Expr) : TrigBase(e) {

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

    /* This works but not when x > 10 or so plus not quite as fast as library version

    private fun custom(x: RealBigDec): Expr {
        val mc = MathContext(RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        var result = x.value.multiply(x.value, mc).plus(BigDecimal.ONE)
        result = x.value.divide(result, mc)

        var yOverX = result
        val y = result.multiply(x.value, mc) // x^2 / (1 + x^2)
        var factEven = BigDecimal(1.0)
        var factOdd = BigDecimal(1.0)

        for(i in 1 until 1000) {
            val n = i * 2
            factEven = factEven.multiply(BigDecimal(n))
            factOdd = factOdd.multiply(BigDecimal(n+1))

            yOverX = yOverX.multiply(y, mc)
            var e = factEven.divide(factOdd, mc)
            e = e.multiply(yOverX, mc)

            val t = result.add(e, mc)
            if (t == result)
                return RealBigDec(result, x.precision)

            result = t
        }

        return RealBigDec(result, x.precision)
    }
     */
}