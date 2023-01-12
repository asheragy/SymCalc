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
}