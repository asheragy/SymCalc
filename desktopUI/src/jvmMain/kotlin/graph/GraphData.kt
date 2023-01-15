package graph

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealDouble

class GraphData(val e: Expr, val xmin: Float, val xmax: Float, steps: Int) {
    val points = getPoints(xmin, xmax, steps)
    val ymin = points.map { it.second }.minOf { it }
    val ymax = points.map { it.second }.maxOf { it }

    private fun getPoints(xmin: Float, xmax: Float, steps: Int): List<Pair<Float, Float>> {
        val result = mutableListOf<Pair<Float, Float>>()
        val step = (xmax - xmin) / steps

        for(i in 0..steps) {
            val x = (xmin + (step * i)).toDouble()
            e.env.setVar("x", RealDouble(x))
            val y = e.eval() as RealDouble
            result.add(Pair(x.toFloat(), y.value.toFloat()))

        }

        return result
    }

    fun getXAxisTicks(): List<Float> {
        val stepSize = xTickDistance()
        val result = mutableListOf<Float>()
        var i = 0

        var start = 0f
        if (xmin < 0) {
            while(start - stepSize >= xmin)
                start -= stepSize
        }
        else if (xmin > 0) {
            while(start + stepSize <= xmin)
                start += stepSize
        }

        while(true) {
            val x = start + (i * stepSize)
            if (x > xmax)
                break

            result.add(x)

            i++
        }

        return result
    }

    fun xTickDistance(): Float {
        val width = xmax - xmin
        return tickDistance(width.toInt()).toFloat()
    }

    private fun tickDistance(n: Int): Int {
        if (n >= 80)
            return 10 * tickDistance(n / 10)
        else if (n >= 40)
            return 10
        else if (n >= 16)
            return 5
        else if (n >= 8)
            return 2
        else
            return 1
    }
}