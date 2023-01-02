package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

class Times(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.Flat.value or Properties.NumericFunction.value or Properties.Orderless.value

    override fun evaluate(): Expr {
        val factors = args.toMutableList()

        var i = 0
        while(i < factors.size - 1) {
            for(j in i+1 until factors.size) {
                var product = multiply(factors[i], factors[j])
                if (product == null)
                    product = multiply(factors[j], factors[i])

                if (product != null) {
                    factors.removeAt(j)
                    factors.removeAt(i)
                    if (!(product is NumberExpr && product.isOne))
                        factors.add(product)
                    i--
                    break
                }
            }

            i++
        }

        if (factors.isEmpty())
            return Integer(1)
        else if (factors.size == 1)
            return factors[0]

        factors.sortWith { x, y ->
            if (x is NumberExpr)
                -1
            else
                0
        }

        return Times(*factors.toTypedArray())
    }

    private fun multiply(a: Expr, b: Expr): Expr? {
        when(a) {
            is NumberExpr -> {
                when(b) {
                    is NumberExpr -> return a * b
                }
                if (a.isOne)
                    return b
                if (a.isZero)
                    return a
            }
            is Plus -> {
                val newArgs = a.args.map { Times(b, it) }
                val e = Plus(*newArgs.toTypedArray()).eval()
                if (e !is Plus) // If Plus eval is not any better don't use this value
                    return e
            }
            is Power -> {
                when(b) {
                    is Power -> {
                        if (a.args[0] == b.args[0])
                            return Power(a.args[0], a.args[1] + b.args[1]).eval()
                        else if(a.args[1] == b.args[1] && a.args.all { it is NumberExpr } && b.args.all { it is NumberExpr })
                            return Power(a.args[0] * b.args[0], a.args[1]).eval()
                    }
                }
                if (a.args[0] == b && b !is NumberExpr)
                    return Power(a.args[0], a.args[1] + Integer(1)).eval()
            }
            is Infinity -> {
                if (b is NumberExpr && b !is Complex)
                    return Infinity(a.direction * if (b.isNegative) -1 else 1)
            }
            is ComplexInfinity -> return ComplexInfinity()
        }

        if(a == b)
            return Power(a, 2)

        return null
    }

    override fun toString(): String {
        if (size > 0) {
            val strings = args.map {
                if (it is Plus || it is Subtract)
                    "($it)"
                else
                    it.toString()
            }

            return strings.joinToString(" * ")
        }

        return super.toString()
    }
}
