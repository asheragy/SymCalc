package expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.ComplexNum

class I : ConstExpr() {
    override fun evaluate(): Expr {
        return ComplexNum(0, 1)
    }
    override fun evaluate(precision: Int): Expr {
        return evaluate()
    }

    override fun toString(): String = "i"
}