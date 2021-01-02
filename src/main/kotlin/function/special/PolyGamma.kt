package org.cerion.symcalc.function.special

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.RealDouble
import org.nevec.rjm.BigDecimalMath
import kotlin.math.abs
import kotlin.math.tan

class PolyGamma(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is RealDouble) {
            if (e.isWholeNumber() && e.isNegative)
                return ComplexInfinity()

            return RealDouble(evalDouble(e.value))
        }

        return this
    }


    private val psi0 = 1.4616321449683622 // PolyGamma(psi0) == 0

    private fun evalDouble(x: Double): Double {
        // Reduce to value near 1
        if (x > 2.0) {
            val m = (x - 0.5).toInt()
            val xmin1 = x - m
            var result = 0.0
            for (i in 1..m)
                result += 1.0 / (x - i)

            return result + evalDouble(xmin1)
        }
        else if (x == psi0)
            return 0.0
        else if (abs(x - psi0) < 0.55) {
            // Pre computed taylor series
            val psiT = doubleArrayOf(9.67672245447621170427e-01, -4.42763168983592106093e-01,
                    2.58499760955651010624e-01, -1.63942705442406527504e-01, 1.07824050691262365757e-01,
                    -7.21995612564547109261e-02, 4.88042881641431072251e-02, -3.31611264748473592923e-02,
                    2.25976482322181046596e-02, -1.54247659049489591388e-02, 1.05387916166121753881e-02,
                    -7.20453438635686824097e-03, 4.92678139572985344635e-03, -3.36980165543932808279e-03,
                    2.30512632673492783694e-03, -1.57693677143019725927e-03, 1.07882520191629658069e-03,
                    -7.38070938996005129566e-04, 5.04953265834602035177e-04, -3.45468025106307699556e-04,
                    2.36356015640270527924e-04, -1.61706220919748034494e-04, 1.10633727687474109041e-04,
                    -7.56917958219506591924e-05, 5.17857579522208086899e-05, -3.54300709476596063157e-05,
                    2.42400661186013176527e-05, -1.65842422718541333752e-05, 1.13463845846638498067e-05,
                    -7.76281766846209442527e-06, 5.31106092088986338732e-06, -3.63365078980104566837e-06,
                    2.48602273312953794890e-06, -1.70085388543326065825e-06, 1.16366753635488427029e-06,
                    -7.96142543124197040035e-07, 5.44694193066944527850e-07, -3.72661612834382295890e-07,
                    2.54962655202155425666e-07, -1.74436951177277452181e-07, 1.19343948298302427790e-07,
                    -8.16511518948840884084e-08, 5.58629968353217144428e-08, -3.82196006191749421243e-08,
                    2.61485769519618662795e-08, -1.78899848649114926515e-08, 1.22397314032336619391e-08,
                    -8.37401629767179054290e-09, 5.72922285984999377160e-09)

            val xdiff = x - psi0
            var result = 0.0
            for (i in psiT.indices.reversed())
                result = result * xdiff + psiT[i]

            return result * xdiff
        }
        else if (x < 0.0) {
            val xmin = 1.0 - x
            return evalDouble(xmin) + Math.PI / tan(Math.PI * xmin)
        }

        val xmin1 = x - 1
        var result = 0.0
        for (k in 26 downTo 1) {
            result -= BigDecimalMath.zeta1(2 * k + 1)
            result *= xmin1 * xmin1
        }

        val c = 0.42278433509846713 // EulerGamma - 1
        return (result + c + 0.5 / xmin1) - 1.0 / (1 - xmin1 * xmin1) - Math.PI / (2.0 * tan(Math.PI * xmin1))
    }
}