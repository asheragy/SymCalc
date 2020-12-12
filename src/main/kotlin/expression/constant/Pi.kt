package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Pi : ConstExpr() {
    override fun toString(): String = "Pi"
    override fun evaluateMachinePrecision() = RealDouble(Math.PI)

    override fun evaluateFixedPrecision(precision: Int): Expr {
        val storedPrecision = RealBigDec.getStoredPrecision(precision)
        if (storedPrecision < PI_DIGITS_1000.length) {
            val t = RealBigDec(PI_DIGITS_1000).toPrecision(storedPrecision) as RealBigDec
            return RealBigDec(t.value, precision)
        }

        return evalCompute(precision)
    }

    fun evalCompute(precision: Int) = RealBigDec(getPiToDigits(precision), precision)

    private fun getPiToDigits(precision: Int): BigDecimal {
        // https://en.wikipedia.org/wiki/Chudnovsky_algorithm
        // 426880*Sqrt(10005)/Pi = Sum (6k!)(545140134k+13591409) / (3k)!(k!)^3(-262537412640768000)^k

        val mc = MathContext(RealBigDec.getStoredPrecision(precision), RoundingMode.HALF_UP)
        val bd54 = BigDecimal(545140134)
        val bd13 = BigDecimal(13591409)
        val bd26 = BigDecimal("-262537412640768000")

        // Calculate sum
        var sum = BigDecimal(0)
        for(k in 0..5000) {
            val n1 = factorial(6 * k)
            var numerator = bd54.multiply(BigDecimal(k))
            numerator = numerator.add(bd13)
            numerator = numerator.multiply(n1)

            val d1 = factorial(3 * k)
            val d2 = factorial(k).pow(3)
            val d3 = bd26.pow(k)
            val denominator = d1.multiply(d2).multiply(d3)

            val next = numerator.divide(denominator, mc)
            val t = sum.add(next, mc)
            if (t == sum) {
                // Divide this constant value by sum to get Pi
                var c = BigDecimalMath.sqrt(BigDecimal("10005"), mc)
                c = c.multiply(BigDecimal("426880"))

                return c.divide(sum, mc)
            }

            sum = t
        }

        throw IterationLimitExceeded()
    }

    private fun factorial(k: Int): BigDecimal {
        var bd = BigDecimal(1.0)
        for(i in 1..k) {
            bd = bd.multiply(BigDecimal(i))
        }

        return bd
    }

    /*
    private var one: BigDecimal = BigDecimal.ZERO
    private var two: BigDecimal = BigDecimal.ZERO
    private var four: BigDecimal = BigDecimal.ZERO

    private fun getPiToDigits2(scale: Int): BigDecimal {
        // Initialize
        one = BigDecimal(1.0).setScale(scale)
        two = BigDecimal(2.0).setScale(scale)
        four = BigDecimal(4.0).setScale(scale)

        var sum = calc(0, scale)

        // FEAT look into another algorithm this one is slow after 2000+ digits
        for(i in 1..10000) {
            val prev = sum
            sum += calc(i, scale)

            if (sum == prev) {
                return sum.setScale(scale - 1, RoundingMode.HALF_UP)
            }
        }

        throw RuntimeException("too many iterations required to calculate")
    }

    // 16-k [ 4/(8k+1) - 2/(8k+4) - 1/(8k+5) - 1/(8k+6) ]
    private fun calc(i: Int, scale: Int): BigDecimal {
        var k = BigDecimal(i).setScale(scale)
        val k8 = k.times(BigDecimal(8.0))

        var result = four / k8.plus(BigDecimal.ONE)
        result -= two / k8.plus(BigDecimal(4.0))
        result -= one / k8.plus(BigDecimal(5.0))
        result -= one / k8.plus(BigDecimal(6.0))

        k = BigDecimal(16).pow(i)
        k = BigDecimal.ONE.divide(k)
        result *= k

        return result.setScale(scale, RoundingMode.HALF_UP)
    }
     */
}

private const val PI_DIGITS_1000 = "3.14159265358979323846264338327950288419716939937510582097494459230781" +
        "6406286208998628034825342117067982148086513282306647093844609550582231" +
        "7253594081284811174502841027019385211055596446229489549303819644288109" +
        "7566593344612847564823378678316527120190914564856692346034861045432664" +
        "8213393607260249141273724587006606315588174881520920962829254091715364" +
        "3678925903600113305305488204665213841469519415116094330572703657595919" +
        "5309218611738193261179310511854807446237996274956735188575272489122793" +
        "8183011949129833673362440656643086021394946395224737190702179860943702" +
        "7705392171762931767523846748184676694051320005681271452635608277857713" +
        "4275778960917363717872146844090122495343014654958537105079227968925892" +
        "3542019956112129021960864034418159813629774771309960518707211349999998" +
        "3729780499510597317328160963185950244594553469083026425223082533446850" +
        "3526193118817101000313783875288658753320838142061717766914730359825349" +
        "0428755468731159562863882353787593751957781857780532171226806613001927" +
        "876611195909216420199"