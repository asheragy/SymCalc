package graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode


fun getPoints(): List<Pair<Float, Float>> {
    return (-10..10 step 1).map {
        val x = it / 1.0
        val y = Math.sin(x)
        //val y = x

        Pair(x.toFloat(), y.toFloat())
    }
}

val chartData = ChartData(getPoints())

class ChartData(val points: List<Pair<Float, Float>>) {
    // Assumes points are ordered by X values
    val xmin = points.first().first
    val xmax = points.last().first
    val xwidth = xmax - xmin

    val ymin = points.map { it.second }.minOf { it }
    val ymax = points.map { it.second }.maxOf { it }
    val yheight = ymax - ymin

    fun getPoints(size: Size): List<Offset> {
        return getPoints(size.width, size.height)
    }

    private fun getPoints(width: Float, height: Float): List<Offset> {
        val leftHalfPercentage = xmax / xwidth
        val centerW = width - (width * leftHalfPercentage)
        //val centerW = width / 2

        val topHalfPercentage = ymax / yheight
        val centerH = height * topHalfPercentage

        // 20 is xmax - xmin
        val x_multiplier = width / xwidth
        val y_multiplier = height / yheight

        return points.map { Offset(it.first * x_multiplier + centerW, centerH - it.second * y_multiplier) }
    }

    fun getXAxisPosition(size: Size): Float {
        val topHalfPercentage = ymax / yheight
        val centerH = Math.max(0f, size.height * Math.min(1f, topHalfPercentage))

        return centerH
    }

    fun getYAxisPosition(size: Size): Float {
        val leftHalfPercentage = Math.max(0f, xmax / xwidth)
        val centerW = Math.max(0f, size.width - (size.width * leftHalfPercentage))

        return centerW
    }

    fun getXAxisTicks(size: Size): List<Float> {
        val x_multiplier = size.width / xwidth
        val centerW = size.width / 2

        // TODO implies viewport of -10 to 10
        return (-10..10).map {
            it * x_multiplier + centerW
        }
    }

    fun getYAxisTicks(size: Size): List<Float> {
        val multiplier = size.height / yheight
        val center = size.height / 2

        return (-10..10).map {
            it * multiplier + center
        }
    }
}


@Composable
fun MyChart() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPoints(
            points = chartData.getPoints(size),
            pointMode = PointMode.Polygon,
            color = Color.Blue,
            strokeWidth = 2.0f
        )

        // X-Axis
        val xAxisPosition = chartData.getXAxisPosition(size)

        drawLine(
            start = Offset(0f, xAxisPosition),
            end = Offset(size.width, xAxisPosition),
            color = Color.Black,
        )

        // Ticks
        chartData.getXAxisTicks(size).forEach {
            drawLine(
                start = Offset(it, y = xAxisPosition - 5),
                end = Offset(it, y = xAxisPosition + 5),
                color = Color.Black,
            )
        }

        // Y-Axis
        val yAxisPosition = chartData.getYAxisPosition(size)

        drawLine(
            start = Offset(yAxisPosition, y = 0f),
            end = Offset(yAxisPosition, y = size.height),
            color = Color.Black,
        )

        // Ticks
        chartData.getYAxisTicks(size).forEach {
            drawLine(
                start = Offset(yAxisPosition - 5, it),
                end = Offset(yAxisPosition + 5, it),
                color = Color.Black,
            )
        }
    }
}