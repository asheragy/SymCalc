package org.cerion.symcalc.expression

import expression.function.list.ConstantArray
import org.cerion.symcalc.Environment
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.parser.Lexer
import org.cerion.symcalc.parser.Parser

interface AtomExpr {
    val value: Any?
}

abstract class MultiExpr(vararg e: Expr) : Expr() {
    val args: List<Expr> = listOf(*e)
    fun getList(index: Int): ListExpr = get(index) as ListExpr
    fun getInteger(index: Int): Integer = get(index) as Integer

    val size get() = args.size
    operator fun get(index: Int): Expr = args[index]
}

abstract class Expr {
    // TODO_LP need to handle this completely differently, global has issues with tests and duplicate values
    var env = Environment()
    protected fun getEnvVar(name: String): Expr? = env.getVar(name)
    protected fun setEnvVar(name: String, e: Expr) = env.setVar(name, e)

    abstract val type: ExprType

    open val precision get() = InfinitePrecision

    val isInteger: Boolean get() = this is Integer
    val isList: Boolean get() = type == ExprType.LIST
    val isError: Boolean get() = type == ExprType.ERROR

    // Casting
    fun asList(): ListExpr = this as ListExpr
    fun asInteger(): Integer = this as Integer
    fun asBool(): BoolExpr = this as BoolExpr

    final override fun equals(other: Any?): Boolean {
        if (other is Expr) {
            return this.equals(other)
        }

        return false
    }

    abstract override fun toString(): String
    abstract fun equals(e: Expr): Boolean

    open fun eval(): Expr = this

    fun eval(precision: Int): Expr {
        return N(this, Integer(precision)).eval()
    }

    fun print(i: Int = 0) {
        if (this is MultiExpr) {
            indent(i, "${this.javaClass.simpleName}: $size")
            for (j in 0 until size)
                get(j).print(i + 1)
        }
        else
            indent(i, "$this")
    }

    enum class ExprType {
        NUMBER,
        VARIABLE,
        FUNCTION,
        LIST,
        CONST,
        ERROR,
        BOOL,
        GRAPHICS,
        SYMBOL
    }

    protected enum class LogicalCompare {
        TRUE,
        FALSE,
        UNKNOWN,
        ERROR
    }

    private fun indent(i: Int, s: String) {
        for (ii in 0 until 2 * i)
            print(" ")
        println(s)
    }

    fun isFunction(name: String): Boolean {
        if (this is FunctionExpr) {
            if (name.compareTo(this.name, ignoreCase = true) == 0)
                return true
        }

        return false
    }

    companion object {
        @JvmStatic
        fun parse(s: String): Expr {
            val lex = Lexer(s)
            val p = Parser(lex)
            return p.e
        }

        const val InfinitePrecision = Int.MAX_VALUE
        const val MachinePrecision = -1 // numbers evaluated to whatever primitive types hold
    }

    // Extensions for convenience
    fun toList(size: Int): ListExpr = ConstantArray(this, Integer(size)).eval().asList()

    override fun hashCode(): Int {
        var result = 0
        if (this is AtomExpr)
            result = value?.hashCode() ?: 0

        if (this is MultiExpr)
            result = 31 * result + (args.hashCode())
        result = 31 * result + type.hashCode()
        return result
    }
}
