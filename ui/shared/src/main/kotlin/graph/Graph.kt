package graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.trig.Sin
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine

val x = VarExpr("x")
//val expr = x
val expr = Sin(VarExpr("x"))

@Composable
fun Graph() {
    val paint = Paint().asFrameworkPaint().apply {
        // paint configuration
    }

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

            drawIntoCanvas {canvas ->
                val textLine = TextLine.Companion.make(it.second, Font())
                canvas.nativeCanvas.drawTextLine(textLine, it.first - (textLine.width / 2), xAxisPosition + 15, paint)
            }
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

            drawIntoCanvas {canvas ->
                val textLine = TextLine.Companion.make(it.second, Font())
                canvas.nativeCanvas.drawTextLine(textLine, yAxisPosition - textLine.width - 5, it.first + (textLine.xHeight / 2), paint)
            }
        }
    }
}
