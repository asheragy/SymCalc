package graph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.cerion.symcalc.expression.Expr


class GraphModel(expr: Expr, val xmin: Float, val xmax: Float, val size: Size) {
    private val plot = GraphData(expr, xmin, xmax, size.width.toInt())

    private val points = plot.points

    //private val xmin = plot.xmin
    //private val xmax = plot.xmax
    private val xwidth = xmax - xmin

    private val ymin = plot.ymin
    private val ymax = plot.ymax
    private val yheight = ymax - ymin

    fun getPoints(): List<Offset> {
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

    fun getXAxisPosition(): Float {
        val topHalfPercentage = ymax / yheight
        val centerH = Math.max(0f, size.height * Math.min(1f, topHalfPercentage))

        return centerH
    }

    fun getYAxisPosition(): Float {
        val leftHalfPercentage = Math.max(0f, xmax / xwidth)
        val centerW = Math.max(0f, size.width - (size.width * leftHalfPercentage))

        return centerW
    }

    fun getXAxisTicks(): List<Float> {
        val x_multiplier = size.width / xwidth
        val centerW = size.width / 2

        // TODO implies viewport of -10 to 10
        return (-10..10).map {
            it * x_multiplier + centerW
        }
    }

    fun getYAxisTicks(): List<Float> {
        val multiplier = size.height / yheight
        val center = size.height / 2

        return (-10..10).map {
            it * multiplier + center
        }
    }
}