import androidx.compose.ui.geometry.Size
import graph.ChartData
import org.junit.Test
import kotlin.test.assertEquals

class GraphTest {
    private val size = Size(800f, 600f)

    @Test
    fun graphCentered() {
        val chart = getChart({x -> x.toFloat()},-10, 10)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))
    }

    @Test
    fun yAxisShift() {
        var chart = getChart({x -> x - 5f}, -5, 15)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(200f, chart.getYAxisPosition(size))

        chart = getChart({x -> x - 10f}, 0, 20)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(0f, chart.getYAxisPosition(size))

        chart = getChart({x -> x - 15f}, 5, 25)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(0f, chart.getYAxisPosition(size))

        // Other direction
        chart = getChart({x -> x + 5f}, -15, 5)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(600f, chart.getYAxisPosition(size))

        chart = getChart({x -> x + 10f}, -20, 0)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(800f, chart.getYAxisPosition(size))

        chart = getChart({x -> x + 15f}, -25, -5)
        assertEquals(300f, chart.getXAxisPosition(size))
        assertEquals(800f, chart.getYAxisPosition(size))
    }

    @Test
    fun xAxisShift() {
        // Shift down
        var chart = getChart({x -> x + 5f}, -10, 10)
        assertEquals(450f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))

        chart = getChart({x -> x + 10f}, -10, 10)
        assertEquals(600f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))

        chart = getChart({x -> x + 15f}, -10, 10)
        assertEquals(600f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))

        // Shift up
        chart = getChart({x -> x - 5f}, -10, 10)
        assertEquals(150f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))

        chart = getChart({x -> x - 10f}, -10, 10)
        assertEquals(0f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))

        chart = getChart({x -> x - 15f}, -10, 10)
        assertEquals(0f, chart.getXAxisPosition(size))
        assertEquals(400f, chart.getYAxisPosition(size))
    }


    private fun getChart(f: (Int) -> Float, min: Int, max: Int): ChartData {
        val points = getPoints(f, min, max)
        return ChartData(points)
    }

    private fun getPoints(f: (Int) -> Float, min: Int, max: Int): List<Pair<Float, Float>> {
        return (min..max step 1).map { x ->
            Pair(x.toFloat(), f(x))
        }
    }
}