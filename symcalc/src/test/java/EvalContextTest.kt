import org.cerion.symcalc.`==`
import org.cerion.symcalc.EvalContext
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertNull

class EvalContextTest {

    @Test
    fun test() {
        val ctx = EvalContext()
        assertNull(ctx["i"])

        ctx["i"] = Integer(3)
        ctx["j"] = Integer(5)

        val ctx2 = ctx.child()
        ctx2["i"]!! `==` Integer(3)

        ctx2.set("i", Integer(2))
        ctx2.assign("j", Integer(4))
        ctx2.set("k", Integer(6))
        ctx2.assign("l", Integer(8))

        ctx["i"]!! `==` 3
        ctx["j"]!! `==` 4
        assertNull(ctx["k"])
        ctx["l"]!! `==` 8

        ctx2["i"]!! `==` 2
        ctx2["j"]!! `==` 4
        ctx2["k"]!! `==` 6
        ctx2["l"]!! `==` 8
    }
}
