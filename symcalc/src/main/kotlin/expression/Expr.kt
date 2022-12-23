package org.cerion.symcalc.expression

import org.cerion.symcalc.Environment
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.function.list.ConstantArray
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import org.cerion.symcalc.parser.Lexer
import org.cerion.symcalc.parser.Parser

interface AtomExpr {
    val value: Any?
}

abstract class MultiExpr(val args: Array<out Expr>) : Expr() {

    fun getList(index: Int): ListExpr = get(index) as ListExpr
    fun getInteger(index: Int): Integer = get(index) as Integer

    val size get() = args.size
    operator fun get(index: Int): Expr = args[index]

    companion object {
        fun convertArgs(vararg e: Any): Array<Expr> {
            return e.map {
                when (it) {
                    is Expr -> it
                    is Long -> Integer(it)
                    is Int -> Integer(it)
                    is Double -> RealDouble(it)
                    is String -> {
                        when {
                            it[0].isLetter() -> VarExpr(it)
                            it.contains('.') -> RealBigDec(it)
                            it.any { a -> a.isDigit() } -> Integer(it) as Expr
                            else -> throw IllegalArgumentException("unrecognized string $it")
                        }
                    }
                    is List<*> -> ListExpr(*it.toTypedArray() as Array<Any>)
                    else -> throw IllegalArgumentException("class ${it.javaClass.simpleName}")
                }
            }.toTypedArray()
        }
    }
}

abstract class Expr {
    // TODO need to handle this completely differently, global has issues with tests and duplicate values
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

        fun parse(start: Expr, input: String): Expr {
            val lex = Lexer(input)
            val p = Parser(lex, start)
            return p.e
        }

        const val InfinitePrecision = Int.MAX_VALUE
        const val MachinePrecision = -1 // numbers evaluated to whatever primitive types hold
    }

    // Extensions for convenience
    fun toList(size: Int): ListExpr = ConstantArray(this, Integer(size)).eval().asList()

    operator fun plus(other: Expr): Expr = Plus(this, other).eval()
    operator fun plus(other: Int): Expr = Plus(this, Integer(other)).eval()
    operator fun minus(other: Expr): Expr = Subtract(this, other).eval()
    open operator fun minus(other: Int): Expr = Subtract(this, Integer(other)).eval()
    operator fun times(other: Expr): Expr = Times(this, other).eval()
    operator fun times(other: Int): Expr = Times(this, Integer(other)).eval()
    operator fun div(other: Expr): Expr = Divide(this, other).eval()
    operator fun div(other: Int): Expr = Divide(this, Integer(other)).eval()

    override fun hashCode(): Int {
        var result = 0
        if (this is AtomExpr)
            result = value?.hashCode() ?: 0

        if (this is MultiExpr) {
            result = 31 * result + (args.contentHashCode())
            if (this is FunctionExpr)
                result = 31 * result + name.hashCode()
        }
        result = 31 * result + type.hashCode()
        return result
    }
}

operator fun Int.plus(other: Expr) = Plus(Integer(this), other).eval()
operator fun Int.minus(other: Expr) = Subtract(Integer(this), other).eval()
operator fun Int.times(other: Expr) = Times(Integer(this), other).eval()