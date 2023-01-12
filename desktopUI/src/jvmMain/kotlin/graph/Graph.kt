package graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.trig.Sin


val expr = Sin(VarExpr("x"))

@Composable
fun Graph() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val model = GraphModel(expr, -10f, 10f, size)

        drawPoints(
            points = model.getPoints(),
            pointMode = PointMode.Polygon,
            color = Color.Blue,
            strokeWidth = 2.0f
        )

        // X-Axis
        val xAxisPosition = model.getXAxisPosition()

        drawLine(
            start = Offset(0f, xAxisPosition),
            end = Offset(size.width, xAxisPosition),
            color = Color.Black,
        )

        // Ticks
        model.getXAxisTicks().forEach {
            drawLine(
                start = Offset(it, y = xAxisPosition - 5),
                end = Offset(it, y = xAxisPosition + 5),
                color = Color.Black,
            )
        }

        // Y-Axis
        val yAxisPosition = model.getYAxisPosition()

        drawLine(
            start = Offset(yAxisPosition, y = 0f),
            end = Offset(yAxisPosition, y = size.height),
            color = Color.Black,
        )

        // Ticks
        model.getYAxisTicks().forEach {
            drawLine(
                start = Offset(yAxisPosition - 5, it),
                end = Offset(yAxisPosition + 5, it),
                color = Color.Black,
            )
        }
    }
}