package org.cerion.math.bignum.extensions

import org.cerion.math.bignum.IterationLimitExceeded
import org.cerion.math.bignum.sqrt
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


fun getPiToDigits(precision: Int): BigDecimal {
    // https://en.wikipedia.org/wiki/Chudnovsky_algorithm
    // 426880*Sqrt(10005)/Pi = Sum (6k!)(545140134k+13591409) / (3k)!(k!)^3(-262537412640768000)^k

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val bd54 = BigDecimal(545140134)
    val bd13 = BigDecimal(13591409)
    val bd26 = BigDecimal("-262537412640768000")

    // Calculate sum
    var sum = bd13
    var n1 = BigDecimal.ONE // (6*k)!
    var d1 = BigDecimal.ONE // (3*k)!
    var d2 = BigDecimal.ONE // k!
    var d3 = BigDecimal.ONE // bd26^k
    for(k in 1..5000) {
        n1 = factorial(6*k, n1, 6*(k-1))
        val numerator = bd54
            .multiply(BigDecimal(k))
            .add(bd13)
            .multiply(n1)

        d1 = factorial(3 * k, d1, 3 * (k-1))
        d2 = factorial(k, d2, k-1)
        d3 = d3.multiply(bd26, mc)
        val denominator = d1.multiply(d2.pow(3)).multiply(d3)

        val next = numerator.divide(denominator, mc)
        val t = sum.add(next, mc)
        if (t == sum) {
            // Divide this constant value by sum to get Pi
            var c = BigDecimal("10005").sqrt(mc.precision)
            c = c.multiply(BigDecimal("426880"))

            return c.divide(sum, mc)
        }

        sum = t
    }

    throw IterationLimitExceeded()
}

private fun factorial(k: Int, prev: BigDecimal, kprev: Int): BigDecimal {
    var bd = prev
    for(i in (kprev+1)..k) {
        bd = bd.multiply(BigDecimal(i))
    }

    return bd
}

fun getEToPrecision(precision: Int): BigDecimal {
    if (precision < E_1000_DIGITS.length) {
        return E_1000.setScale(precision - 1, RoundingMode.HALF_UP)
    }

    return calculateE(precision)
}

fun calculateE(precision: Int): BigDecimal {
    var e = BigDecimal.ONE
    var next = BigDecimal.ONE
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    var i = 2
    // This seems to work for ending loop
    // scale >= precision*2 means the first half of the decimal places are all zeros and should no longer be effecting the result
    while (next.scale() - precision < precision) {
        e = e.add(next, mc)
        next = next.divide(BigDecimal(i), mc)
        i++
    }

    return e
}

private const val E_1000_DIGITS = "2.71828182845904523536028747135266249775724709369995957496696762772407663035354" +
        "759457138217852516642742746639193200305992181741359662904357290033429526059563" +
        "073813232862794349076323382988075319525101901157383418793070215408914993488416" +
        "750924476146066808226480016847741185374234544243710753907774499206955170276183" +
        "860626133138458300075204493382656029760673711320070932870912744374704723069697" +
        "720931014169283681902551510865746377211125238978442505695369677078544996996794" +
        "686445490598793163688923009879312773617821542499922957635148220826989519366803" +
        "318252886939849646510582093923982948879332036250944311730123819706841614039701" +
        "983767932068328237646480429531180232878250981945581530175671736133206981125099" +
        "618188159304169035159888851934580727386673858942287922849989208680582574927961" +
        "048419844436346324496848756023362482704197862320900216099023530436994184914631" +
        "409343173814364054625315209618369088870701676839642437814059271456354906130310" +
        "720851038375051011574770417189861068739696552126715468895703503540212340784981" +
        "933432106817012100562788023519303322474501585390473041995777709350366041699732" +
        "972508868769664035557071622684471625607988265178713419512466520103059212366771" +
        "943252786753985589448969709640975459185695638023637016211204774272283648961342" +
        "251644507818244235294863637214174023889344124796357437026375529444833799801612" +
        "549227850925778256209262264832627793338656648162772516401910590049164499828931"

private val E_1000 = BigDecimal(E_1000_DIGITS)