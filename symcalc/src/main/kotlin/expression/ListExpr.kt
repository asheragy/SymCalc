package org.cerion.symcalc.expression

import org.cerion.symcalc.function.list.Join
import org.cerion.symcalc.number.NumberExpr

class ListExpr(vararg e: Any) : MultiExpr(convertArgs(*e)) {
    override val type: ExprType get() = ExprType.LIST

    constructor() : this(*emptyArray<Expr>())
    constructor(items: List<Expr>) : this(*items.toTypedArray())

    constructor(vararg n: Number): this(*n.map { NumberExpr.create(it) }.toTypedArray())

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
    override fun eval(): ListExpr = ListExpr(*args.map { it.eval() }.toTypedArray())

    // Helper functions
    fun join(vararg list: ListExpr): ListExpr = Join(this, *list).eval() as ListExpr

    fun map(transform: (Expr) -> Expr): ListExpr {
        val t = args.mapTo(ArrayList(size), transform)
        return ListExpr(t)
    }
}
