package expression

import expression.function.list.ConstantArray
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum

fun Expr.toList(size: Int): ListExpr {
    return ConstantArray(this, IntegerNum(size)).eval().asList()
}