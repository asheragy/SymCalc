package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.NumberExpr

class ListExpr(vararg e: Expr) : Expr() {
    override val value: Any? get() = null
    override val type: ExprType get() = ExprType.LIST

    constructor() : this(*emptyArray<Expr>())

    init {
        setArgs(*e)
    }

    constructor(vararg n: Number): this(*n.map { NumberExpr.create(it) }.toTypedArray())

    operator fun plusAssign(e: Expr) = add(e)

    fun add(t: Expr) {
        addArg(t)
    }

    fun addAll(items: List<Expr>?) {
        if (items != null) {
            for (e in items) {
                add(e)
            }
        }
    }

    override fun equals(e: Expr): Boolean {
        if (e is ListExpr) {
            if (e.size == size) {
                for (i in 0 until e.size) {
                    if (!e[i].equals(get(i)))
                        return false
                }

                return true
            }
        }

        return false
    }

    override fun toString(): String = "{" + args.joinToString(", ") + "}"
    override fun evaluate(): ListExpr = ListExpr(*args.map { it.eval() }.toTypedArray())

    override fun treeForm(i: Int) {
        indent(i, "List: $size")
        for (j in 0 until size)
            get(j).treeForm(i + 1)
    }
}
