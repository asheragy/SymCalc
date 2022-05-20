package org.cerion.symcalc.constant

import org.cerion.math.bignum.decimal.calculateE
import org.cerion.math.bignum.decimal.getEToPrecision
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble

class E : ConstExpr() {

    override fun toString(): String = "E"
    override fun evaluateMachinePrecision() = RealDouble(Math.E)

    override fun evaluateFixedPrecision(precision: Int): Expr {
        val storedPrecision = RealBigDec.getStoredPrecision(precision)
        return RealBigDec(getEToPrecision(storedPrecision), precision)
    }

    internal fun evalCompute(precision: Int): RealBigDec {
        return RealBigDec(calculateE(RealBigDec.getStoredPrecision(precision)), precision)
    }
}