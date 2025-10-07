package graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.trig.Sin

val x = VarExpr("x")
val expr = Sin(x)

@Composable
fun Graph() {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 12.sp, color = Color.Black)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val model = GraphModel(expr, -10f, 10f, size)
        //val model = GraphModel(expr, -size.width / 20, size.width / 20, size)

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
                start = Offset(it.first, y = xAxisPosition - 5),
                end = Offset(it.first, y = xAxisPosition),
                color = Color.Black,
            )

            val textLayoutResult = textMeasurer.measure(it.second, textStyle)
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(it.first - (textLayoutResult.size.width / 2), xAxisPosition + 15)
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
                start = Offset(yAxisPosition, it.first),
                end = Offset(yAxisPosition + 5, it.first),
                color = Color.Black,
            )

            val textLayoutResult = textMeasurer.measure(it.second, textStyle)
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(yAxisPosition - textLayoutResult.size.width - 5, it.first - (textLayoutResult.size.height / 2))
            )
        }
    }
}
