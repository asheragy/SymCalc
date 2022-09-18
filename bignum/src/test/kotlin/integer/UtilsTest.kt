package integer

import org.cerion.math.bignum.integer.BinomialGenerator
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


internal class UtilsTest {

    @Test
    fun binomialGenerator() {
        val generator = BinomialGenerator(5)
        assertEquals(1, generator[0].toInt())
        assertThrows<IndexOutOfBoundsException> { generator[1] }

        generator.inc()
        assertEquals(1, generator[0].toInt())
        assertEquals(1, generator[1].toInt())
        assertThrows<IndexOutOfBoundsException> { generator[2] }

        generator.inc()
        assertEquals(1, generator[0].toInt())
        assertEquals(2, generator[1].toInt())
        assertEquals(1, generator[2].toInt())
        assertThrows<IndexOutOfBoundsException> { generator[3] }

        generator.inc()
        assertEquals(1, generator[0].toInt())
        assertEquals(3, generator[1].toInt())
        assertEquals(3, generator[2].toInt())
        assertEquals(1, generator[3].toInt())
        assertThrows<IndexOutOfBoundsException> { generator[4] }

        generator.inc()
        assertEquals(1, generator[0].toInt())
        assertEquals(4, generator[1].toInt())
        assertEquals(6, generator[2].toInt())
        assertEquals(4, generator[3].toInt())
        assertEquals(1, generator[4].toInt())
        assertThrows<IndexOutOfBoundsException> { generator[5] }
    }
}