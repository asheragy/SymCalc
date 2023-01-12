
import androidx.compose.ui.geometry.Size
import graph.GraphModel
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.junit.Test
import kotlin.test.assertEquals

class GraphModelTest {
    private val size = Size(800f, 600f)
    private val x = VarExpr("x")

    @Test
    fun graphCentered() {
        val chart = getGraph(x,-10, 10)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())
    }

    @Test
    fun yAxisShift() {
        var chart = getGraph(x - 5, -5, 15)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(200f, chart.getYAxisPosition())

        chart = getGraph(x - 10, 0, 20)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(0f, chart.getYAxisPosition())

        chart = getGraph(x - 15, 5, 25)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(0f, chart.getYAxisPosition())

        // Other direction
        chart = getGraph(x + 5, -15, 5)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(600f, chart.getYAxisPosition())

        chart = getGraph(x + 10, -20, 0)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(800f, chart.getYAxisPosition())

        chart = getGraph(x + 15, -25, -5)
        assertEquals(300f, chart.getXAxisPosition())
        assertEquals(800f, chart.getYAxisPosition())
    }

    @Test
    fun xAxisShift() {
        // Shift down
        var chart = getGraph(x + 5, -10, 10)
        assertEquals(450f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())

        chart = getGraph(x + 10, -10, 10)
        assertEquals(600f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())

        chart = getGraph(x + 15, -10, 10)
        assertEquals(600f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())

        // Shift up
        chart = getGraph(x - 5, -10, 10)
        assertEquals(150f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())

        chart = getGraph(x - 10, -10, 10)
        assertEquals(0f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())

        chart = getGraph(x - 15, -10, 10)
        assertEquals(0f, chart.getXAxisPosition())
        assertEquals(400f, chart.getYAxisPosition())
    }

    private fun getGraph(expr: Expr, min: Int, max: Int): GraphModel {
        return GraphModel(expr, min.toFloat(), max.toFloat(), size)
    }
}