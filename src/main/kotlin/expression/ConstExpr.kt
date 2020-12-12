package org.cerion.symcalc.expression

import org.cerion.symcalc.constant.*

import java.util.Hashtable

abstract class ConstExpr() : Expr() {
    abstract override fun toString(): String
    override val type: ExprType get() = ExprType.CONST
    override val precision: Int get() = InfinitePrecision

    override fun equals(e: Expr): Boolean = javaClass == e.javaClass

    final override fun eval(): Expr = evaluateInfinitePrecision()

    open fun evaluateInfinitePrecision(): Expr = this
    open fun evaluateMachinePrecision(): Expr = this
    open fun evaluateFixedPrecision(precision: Int): Expr = this

    fun evaluate(precision: Int): Expr {
        return when(precision) {
            MachinePrecision -> evaluateMachinePrecision()
            InfinitePrecision -> evaluateInfinitePrecision()
            else -> evaluateFixedPrecision(precision)
        }
    }

    private enum class Name {
        PI,
        E
    }

    companion object {

        @JvmStatic fun isConstant(s: String): Boolean {
            return lookup(s) != null
        }

        @JvmStatic fun getConstant(name: String): ConstExpr {
            when (this.lookup(name)) {
                Name.PI -> return Pi()
                Name.E -> return E()
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
