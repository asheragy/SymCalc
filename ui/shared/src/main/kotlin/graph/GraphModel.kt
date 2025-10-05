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

    fun getXAxisTicks(): List<Pair<Float, String>> {
        val ticks = plot.getXAxisTicks()
        val round = ticks[1] - ticks[0] >= 1

        val x_offset = ((plot.xmax - plot.xmin) / 2) - plot.xmax
        val x_multiplier = size.width / xwidth
        val centerW = (size.width / 2) + x_offset * x_multiplier

        return ticks
            .filter { it != 0f }
            .map { x ->
            Pair(x * x_multiplier + centerW, if(round) x.toInt().toString() else x.toString())
        }
    }

    fun getYAxisTicks(): List<Pair<Float, String>> {
        val ticks = plot.getYAxisTicks()
        val round = ticks[1] - ticks[0] >= 1

        val offset = ((plot.ymax - plot.ymin) / 2) - plot.ymax
        val multiplier = size.height / yheight
        val center = (size.height / 2) + offset * multiplier

        return ticks
            .filter { it != 0f }
            .map {y ->
                Pair(center - y * multiplier, if(round) y.toInt().toString() else y.toString())
            }
    }
}