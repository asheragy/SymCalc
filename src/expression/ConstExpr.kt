package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.constant.*

import java.util.Hashtable

abstract class ConstExpr : Expr() {
    override val value: Any? get() = null
    override val type: ExprType get() = ExprType.CONST
    override val properties: Int get() = Expr.Properties.CONSTANT.value
    override val precision: Int get() = InfinitePrecision

    override fun treeForm(i: Int) = indent(i, "Constant: " + toString())
    override fun equals(e: Expr): Boolean = javaClass == e.javaClass

    abstract fun evaluate(precision: Int): Expr
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
                ConstExpr.Name.E -> return E()
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
