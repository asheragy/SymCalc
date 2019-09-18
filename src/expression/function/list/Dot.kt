package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Times

class Dot(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val a = getList(0)
        val b = getList(1)
        return if (VectorQ(a).eval().asBool() === BoolExpr.TRUE) evalVector(a, b) else evalMatrix(a, b)
    }

    private fun evalVector(a: ListExpr, b: ListExpr): Expr {
        return Plus(*a.args.mapIndexed { index, expr -> Times(expr,b[index]) }.toTypedArray()).eval()
    }

    private fun evalMatrix(a: ListExpr, b: ListExpr): Expr {
        val items = mutableListOf<Expr>()

        for (i in 0 until a.size) {
            val ax = a.getList(i)
            val subItems = mutableListOf<Expr>()

            for (j in 0 until a.size) {
                val sum = Plus(*ax.args.mapIndexed { index, expr -> Times(expr,b[index][j]) }.toTypedArray())
                subItems.add(sum.eval())
            }

            items.add(ListExpr(subItems))
        }

        return ListExpr(items)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, ExprType.LIST)
        validateParameterType(1, ExprType.LIST)

        val a = getList(0)
        val b = getList(1)

        if (VectorQ(a).eval().asBool().value && VectorQ(b).eval().asBool().value) {
            if (a.size != b.size)
                throw ValidationException("Vectors must be same length")
        } else if (MatrixQ(a).eval().asBool().value && MatrixQ(b).eval().asBool().value) {
            val ax = a.getList(0)
            val bx = b.getList(0)

            if (a.size != bx.size || b.size != ax.size)
                throw ValidationException("Incompatible matrix sizes")
        } else {
            throw ValidationException("Arrays must be the same rank")
        }
    }
}
