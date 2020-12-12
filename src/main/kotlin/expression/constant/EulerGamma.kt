package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.integer.Bernoulli
import kotlin.math.max

class EulerGamma : ConstExpr() {
    override fun toString() = "EulerGamma"

    override fun evaluateMachinePrecision() = RealDouble(0.5772156649015329)

    override fun evaluateFixedPrecision(precision: Int): Expr {
        if (precision <= EULER_GAMMA_1000.length - 2)
            return RealBigDec(EULER_GAMMA_1000).toPrecision(precision)

        return evaluateCompute(precision)
    }

    // Using formula f[n] =
    //  (2 n - 1)/(2 n) - Log[n] + Sum[1/k (1 + BernoulliB[k]/n^k), {k, 2, n}]
    internal fun evaluateCompute(precision: Int): Expr {
        var sum: Expr = Integer(0)
        val n = Integer(max(2,precision))
        var npowk = n

        for(i in 1 until n.value.toInt()) {
            val k = Integer(i+1)
            npowk *= n
            sum += (((Bernoulli(k) / npowk) + Integer.ONE) / k).eval(precision)
        }

        val divlog = ((n * 2 -1) / (n * 2)) - Log(n)
        return divlog + sum
    }
}

private const val EULER_GAMMA_1000 = "0.57721566490153286060651209008240243104215933593992359880576723488486" +
        "7726777664670936947063291746749514631447249807082480960504014486542836" +
        "2241739976449235362535003337429373377376739427925952582470949160087352" +
        "0394816567085323315177661152862119950150798479374508570574002992135478" +
        "6146694029604325421519058775535267331399254012967420513754139549111685" +
        "1028079842348775872050384310939973613725530608893312676001724795378367" +
        "5927135157722610273492913940798430103417771778088154957066107501016191" +
        "6633401522789358679654972520362128792265559536696281763887927268013243" +
        "1010476505963703947394957638906572967929601009015125195950922243501409" +
        "3498712282479497471956469763185066761290638110518241974448678363808617" +
        "4945516989279230187739107294578155431600500218284409605377243420328547" +
        "8367015177394398700302370339518328690001558193988042707411542227819716" +
        "5230110735658339673487176504919418123000406546931429992977795693031005" +
        "0308630341856980323108369164002589297089098548682577736428825395492587" +
        "3629596133298574739302"