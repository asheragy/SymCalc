package graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode


fun getPoints(): List<Pair<Float, Float>> {
    return (-10..10 step 1).map {
        val x = it / 1.0
        //val y = Math.sin(x)
        val y = x

        Pair(x.toFloat(), y.toFloat())
    }
}

val chartData = ChartData(getPoints())

class ChartData(val points: List<Pair<Float, Float>>) {
    val xMin = points.map { it.first }.minOf { it }
    val xMax = points.map { it.first }.maxOf { it }
    val xWidth = xMax - xMin

    val yMin = points.map { it.second }.minOf { it }
    val yMax = points.map { it.second }.maxOf { it }
    val yHeight = yMax - yMin

    fun getPoints(width: Float, height: Float): List<Offset> {
        val centerW = width / 2
        val centerH = height / 2

        // 20 is xmax - xmin
        val x_multiplier = width / chartData.xWidth
        val y_multiplier = height / chartData.yHeight

        return chartData.points.map { Offset(it.first * x_multiplier + centerW, centerH - it.second * y_multiplier) }
    }

    fun getXAxisTicks(width: Float): List<Float> {
        val x_multiplier = width / chartData.xWidth
        val centerW = width / 2

        // TODO implies viewport of -10 to 10
        return (-10..10).map {
            it * x_multiplier + centerW
        }
    }

    fun getYAxisTicks(height: Float): List<Float> {
        val multiplier = height / yHeight
        val center = height / 2

        return (-10..10).map {
            it * multiplier + center
        }
    }
}


@Composable
fun MyChart() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerW = canvasWidth / 2
        val centerH = canvasHeight / 2

        drawPoints(
            points = chartData.getPoints(canvasWidth, canvasHeight),
            pointMode = PointMode.Polygon,
            color = Color.Blue,
            strokeWidth = 2.0f
        )

        // Draw axis
        drawLine(
            start = Offset(centerW, y = 0f),
            end = Offset(centerW, y = canvasHeight),
            color = Color.Black,
        )

        drawLine(
            start = Offset(0f, centerH),
            end = Offset(canvasWidth, centerH),
            color = Color.Black,
        )

        // Draw x axis ticks
        chartData.getXAxisTicks(canvasWidth).forEach {
            drawLine(
                start = Offset(it, y = centerH - 5),
                end = Offset(it, y = centerH),
                color = Color.Black,
            )
        }

        // Y-axis ticks
        chartData.getYAxisTicks(canvasHeight).forEach {
            drawLine(
                start = Offset(centerW, it),
                end = Offset(centerW + 5, it),
                color = Color.Black,
            )
        }
    }
}