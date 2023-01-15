import graph.GraphData
import org.cerion.symcalc.expression.VarExpr
import org.junit.Test
import kotlin.test.assertEquals

class GraphDataTest {
    private val x = VarExpr("x")

    @Test
    fun xAxisTicks() {
        var data = GraphData(x, -10f, 10f, 10)
        assertEquals(listOf(-10.0f, -5.0f, 0.0f, 5.0f, 10.0f), data.getXAxisTicks())

        data = GraphData(x, -11f, 11f, 10)
        assertEquals(listOf(-10.0f, -5.0f, 0.0f, 5.0f, 10.0f), data.getXAxisTicks())

        data = GraphData(x, -10f, 20f, 10)
        assertEquals(listOf(-10.0f, -5.0f, 0.0f, 5.0f, 10.0f, 15f, 20f), data.getXAxisTicks())

        data = GraphData(x, -25f, -5f, 10)
        assertEquals(listOf(-25.0f, -20.0f, -15.0f, -10.0f, -5.0f), data.getXAxisTicks())

        data = GraphData(x, -26f, -4f, 10)
        assertEquals(listOf(-25.0f, -20.0f, -15.0f, -10.0f, -5.0f), data.getXAxisTicks())

        data = GraphData(x, 5f, 25f, 10)
        assertEquals(listOf(5.0f, 10.0f, 15.0f, 20.0f, 25f), data.getXAxisTicks())
    }
}