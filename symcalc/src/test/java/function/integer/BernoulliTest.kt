package org.cerion.symcalc.function.integer

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble
import org.cerion.symcalc.`should equal`
import org.junit.jupiter.api.Disabled
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class BernoulliTest {

    @Test
    fun validate() {
        // Single integer parameter
        assertEquals(ErrorExpr::class, Bernoulli(RealDouble(2.5)).eval()::class)
        assertEquals(ErrorExpr::class, Bernoulli(Integer(1), Integer(2)).eval()::class)
    }

    @Test
    fun basic() {
        assertAll(
                Bernoulli(Integer(0)) `==` Integer(1),
                Bernoulli(Integer(1)) `==` Rational(-1,2),
                Bernoulli(Integer(2)) `==` Rational(1,6),
                Bernoulli(Integer(3)) `==` Integer.ZERO,
                Bernoulli(Integer(4)) `==` Rational(-1, 30),
                Bernoulli(Integer(20)) `==` Rational(-174611, 330),
                Bernoulli(Integer(30)) `==` Rational(Integer("8615841276005"), Integer("14322")),
                //Bernoulli(Integer(50)) `==` Rational(Integer("495057205241079648212477525"), Integer("66")),
                //Bernoulli(Integer(100)) `==` Rational(Integer("-94598037819122125295227433069493721872702841533066936133385696204311395415197247711"), Integer("33330"))
        )
    }

    @Test
    @Ignore
    fun large() {
        Bernoulli(Integer(300)).eval() `should equal` Rational(Integer("-1863878995204859011995045341848156066182191846635905937518715320655775" +
                "9581743605231349907569223034108104826005287694796420210012184158790061" +
                "6430295537046082914643480796471773719535693514415158342483315425004774" +
                "7433575584999029126775186293388721514970183351129809976971603227633930" +
                "4349238439848295803115933725653985747628800282891676355700124156069413" +
                "67995702212211519561707046505473575241"), Integer("866054419230"))
    }

    @Test
    @Ignore
    // ~800ms
    fun bernoulli() {
        val a = Bernoulli(110)
        repeat(250) {
            a.eval()
        }
    }
}