package org.cerion.symcalc.function.trig

import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

private val one = Integer.ONE
private val i = Complex(0, 1)

class ArcSec(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val z = get(0)
        return ArcCos(Divide(1, z)).eval()

        // Log formula
        //    val sqrt = Sqrt(one - (one / Power(z, 2)))
        //    return i.unaryMinus() * Log(i * sqrt + Divide(one, z))
    }
}

class ArcCsc(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val z = get(0)

        if (z is NumberExpr)
            return ArcSin(Divide(1, z)).eval()

        return this
    }
}

class ArcCot(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val z = get(0)

        if (z is NumberExpr && z.isZero)
            return Divide(Pi(), 2).eval(z.precision)

        return ArcTan(one / z).eval()
    }
}

