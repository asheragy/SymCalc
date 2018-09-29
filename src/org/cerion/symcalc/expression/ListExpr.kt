package org.cerion.symcalc.expression

class ListExpr : Expr {
    override val type: ExprType
        get() = ExprType.LIST

    constructor()

    constructor(t: Expr) {
        setArg(0, t)
    }

    constructor(vararg e: Expr) {
        setArgs(*e)
    }

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
        if (e.isList) {
            val list = e.asList()
            if (list.size() == size()) {
                for (i in 0 until list.size()) {
                    if (!list[i].equals(get(i)))
                        return false
                }

                return true
            }
        }

        return false
    }

    override fun evaluate(): ListExpr = this

    override fun show(i: Int) {
        indent(i, "List: " + size())
        for (j in 0 until size())
            get(j).show(i + 1)
    }

    override fun toString(): String {
        var ret = "{"
        for (i in 0 until size()) {
            if (i > 0) ret += "," //dont add comma on first element
            //Eval before printing
            ret += get(i).toString()
        }

        ret += "}"
        return ret
    }
}
