import org.cerion.math.bignum.decimal.getPiToDigits
import kotlin.test.Test

class ConstantTest {

    @Test
    fun piLarge() {
        getPiToDigits(5500)
    }

    @Test
    fun piMultiple() {
        for(i in 0 until 25000)
            getPiToDigits(100)
    }
}