import org.cerion.math.bignum.binomial
import kotlin.test.Test

class IntegerTest {

    @Test
    // ~630ms
    fun binomial() {
        repeat(600) {
            binomial(500)
        }
    }
}