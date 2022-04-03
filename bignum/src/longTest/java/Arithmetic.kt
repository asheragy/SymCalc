import org.cerion.math.bignum.exp
import org.cerion.math.bignum.log
import org.cerion.math.bignum.root
import org.cerion.math.bignum.sqrt
import org.junit.Test
import java.math.BigDecimal

private const val MIN_PRECISION = 100

class Arithmetic {

    @Test
    fun `log large close to 1`() {
        val x = BigDecimal("1.1")
        x.log(3300)
    }

    @Test
    fun `log large close to 1 multiple`() {
        val x = BigDecimal("1.1")
        for(i in 0 until 8500)
            x.log(MIN_PRECISION)
    }

    @Test
    fun `log large`() {
        val x = BigDecimal("100.1")
        x.log(3000)
    }

    @Test
    fun `log large with small value`() {
        val x = BigDecimal("0.00123")
        x.log(800)
    }

    /*
    @Test
    fun power_multiple() {
        val x = BigDecimal("3.14", MIN_PRECISION)
        for(i in 0 until 2200)
            x.pow(x)
    }

    @Test
    fun power_large() {
        val x = RealBigDec("3.14", 3000)
        x.pow(x)
    }
     */

    @Test
    fun sqrtLarge() {
        val x = BigDecimal("3.0")
        x.sqrt(100000)
    }

    @Test
    fun sqrtMultiple() {
        val x = BigDecimal("3.0")
        for(i in 0 until 200000)
            x.sqrt(MIN_PRECISION)
    }

    @Test
    fun nthRoot() {
        val x = BigDecimal("11.0")
        repeat(52000) {
            x.root(7, MIN_PRECISION)
        }
    }

    @Test
    fun nthRootLarge() {
        val x = BigDecimal("11.0")
        x.root(7, 58000)
    }

    @Test
    fun exp() {
        val x = BigDecimal("11.0")
        repeat(12000) {
            x.exp(MIN_PRECISION)
        }
    }

    @Test
    fun expLarge() {
        val x = BigDecimal("11.0")
        x.exp(3700)
    }
}