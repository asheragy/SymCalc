package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.constant.Pi

import java.util.Hashtable

abstract class ConstExpr : Expr() {

    override fun getType(): Expr.ExprType = Expr.ExprType.CONST
    override fun show(i: Int) = indent(i, "Constant: " + toString())
    override fun equals(e: Expr): Boolean = javaClass == e.javaClass
    override fun getProperties(): Int = Expr.Properties.CONSTANT.value

    abstract override fun toString(): String

    private enum class Name {
        PI,
        E
    }

    companion object {

        @JvmStatic fun isConstant(s: String): Boolean {
            return lookup(s) != null
        }

        @JvmStatic fun getConstant(name: String): ConstExpr {
            val n = lookup(name)
            when (n) {
                ConstExpr.Name.PI -> return Pi()
            }

            throw IllegalArgumentException("invalid constant")
        }

        private var identifiers: Hashtable<String, Name>? = null

        init {
            identifiers = Hashtable()
            identifiers!!["pi"] = Name.PI
            identifiers!!["e"] = Name.E
        }

        private fun lookup(s: String): Name? {
            val lookup = s.toLowerCase()
            return if (identifiers!!.containsKey(lookup)) identifiers!![lookup] else null

        }
    }
}
