package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import java.math.BigDecimal
import java.math.RoundingMode

private val E_1000_DIGITS = "2.71828182845904523536028747135266249775724709369995957496696762772407663035354" +
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

class E : ConstExpr() {

    override fun toString(): String = "E"

    override fun evaluate(): Expr {
        return evaluate(InfinitePrecision)
    }

    override fun evaluate(precision: Int): Expr {
        if (precision < InfinitePrecision) {
            if (precision == MachinePrecision)
                return RealDouble(Math.E)

            // TODO add this when precision < predefined value
            //return RealBigDec(E_1000_DIGITS.substring(0, precision + 10)).toPrecision(precision)
            return RealBigDec(getEToDigits(RealBigDec.getStoredPrecision(precision)), precision)
        }
        else
            return this
    }

    private fun getEToDigits(n: Int): BigDecimal {
        var e: BigDecimal = BigDecimal.ONE.setScale(n)
        var next: BigDecimal = BigDecimal.ONE.setScale(n)

        var i = 2
        while (next.compareTo(BigDecimal.ZERO) != 0) {
            e += next
            next = next.divide(BigDecimal(i).setScale(n), RoundingMode.HALF_UP)
            i++
        }

        return e
    }
}