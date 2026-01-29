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
        val stepSize = tickStepSize(xmax - xmin)
        return getAxisTicks(xmin, xmax, stepSize)
    }

    fun getYAxisTicks(): List<Float> {
        val stepSize = tickStepSize(ymax - ymin)
        return getAxisTicks(ymin, ymax, stepSize)
    }

    private fun getAxisTicks(min: Float, max: Float, stepSize: Float): List<Float> {
        val result = mutableListOf<Float>()
        var i = 0

        var start = 0f
        if (min < 0) {
            while(start - stepSize >= min)
                start -= stepSize
        }
        else if (min > 0) {
            while(start + stepSize <= min)
                start += stepSize
        }

        while(true) {
            val x = start + (i * stepSize)
            if (x > max)
                break

            result.add(x)

            i++
        }

        return result
    }

    companion object {
        fun tickStepSize(n: Float): Float {
            if (n >= 80)
                return 10 * tickStepSize(n / 10)
            else if (n >= 40)
                return 10f
            else if (n >= 16)
                return 5f
            else if (n >= 8)
                return 2f
            else if (n >= 4)
                return 1f
            else
                return tickStepSize(n * 10) / 10
        }
    }
}