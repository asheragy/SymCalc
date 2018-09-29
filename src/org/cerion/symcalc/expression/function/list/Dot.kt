package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Times

class Dot(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.DOT, *e) {

    override fun evaluate(): Expr {
        val a = getList(0)
        val b = getList(1)
        return if (VectorQ(a).eval().asBool() === BoolExpr.TRUE) evalVector(a, b) else evalMatrix(a, b)
    }

    private fun evalVector(a: ListExpr, b: ListExpr): Expr {
        val sum = Plus()
        for (i in 0 until a.size())
            sum.add(Times(a.get(i), b.get(i)))

        return sum.eval()
    }

    private fun evalMatrix(a: ListExpr, b: ListExpr): Expr {
        val result = ListExpr()

        for (i in 0 until a.size()) {
            val ax = a.getList(i)
            val sublist = ListExpr()

            for (j in 0 until a.size()) {
                val sum = Plus()

                for (k in 0 until ax.size())
                    sum.add(Times(
                            ax.get(k),
                            b.get(k).get(j)
                    ))

                sublist.add(sum.eval())
            }

            result.add(sublist)
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, Expr.ExprType.LIST)
        validateParameterType(1, Expr.ExprType.LIST)

        val a = getList(0)
        val b = getList(1)

        if (VectorQ(a).eval().asBool().value() && VectorQ(b).eval().asBool().value()) {
            if (a.size() != b.size())
                throw ValidationException("Vectors must be same length")
        } else if (MatrixQ(a).eval().asBool().value() && MatrixQ(b).eval().asBool().value()) {
            val ax = a.getList(0)
            val bx = b.getList(0)

            if (a.size() != bx.size() || b.size() != ax.size())
                throw ValidationException("Incompatible matrix sizes")
        } else {
            throw ValidationException("Arrays must be the same rank")
        }
    }
}
