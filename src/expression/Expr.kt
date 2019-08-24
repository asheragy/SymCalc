package org.cerion.symcalc.expression

import expression.function.list.ConstantArray
import org.cerion.symcalc.Environment
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealDouble
import org.cerion.symcalc.parser.Lexer
import org.cerion.symcalc.parser.Parser

abstract class Expr {

    abstract val value: Any?

    var env = Environment()
        private set

    private var mArgs: MutableList<Expr>? = null
    val args: List<Expr>
        get() = if(mArgs != null) mArgs!! else emptyList()

    protected fun getEnvVar(name: String): Expr? = env.getVar(name)
    protected fun setEnvVar(name: String, e: Expr) = env.setVar(name, e)

    abstract val type: ExprType

    open val precision get() = InfinitePrecision

    val size get() = if (mArgs != null) mArgs!!.size else 0

    val isInteger: Boolean get() = this is Integer
    val isList: Boolean get() = type == ExprType.LIST
    val isError: Boolean get() = type == ExprType.ERROR

    protected fun setArgs(vararg args: Expr) {
        if (mArgs == null)
            mArgs = ArrayList()

        for (k in args) {
            mArgs!!.add(k)
        }
    }

    // Indexes
    operator fun get(index: Int): Expr = args[index]
    operator fun set(index: Int, e: Expr) {
        mArgs!![index] = e
    }

    fun getList(index: Int): ListExpr = get(index) as ListExpr
    fun getInteger(index: Int): Integer = get(index) as Integer

    // Casting
    fun asList(): ListExpr = this as ListExpr
    fun asInteger(): Integer = this as Integer
    fun asBool(): BoolExpr = this as BoolExpr
    fun asNumber(): NumberExpr = this as NumberExpr

    protected fun setArg(index: Int, e: Expr) {
        if (mArgs == null)
            mArgs = ArrayList()

        if (index == mArgs!!.size)
            mArgs!!.add(e)
        else
            mArgs!![index] = e
    }

    protected fun addArg(e: Expr) {
        setArg(size, e)
    }

    protected fun argString(): String {
        return args.toString()
    }

    final override fun equals(other: Any?): Boolean {
        if (other is Expr) {
            return this.equals(other)
        }

        return false
    }

    abstract override fun toString(): String
    abstract fun treeForm(i: Int)
    abstract fun equals(e: Expr): Boolean
    protected abstract fun evaluate(): Expr

    fun eval(): Expr {
        // not sure if this improves anything but helps with debugging
        if (this is Integer || this is RealDouble || this is BoolExpr)
            return this

        //https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
        //https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

        // Set environment for every parameter to the current one before its evaluated
        for (i in 0 until size) {
            args[i].env = env
        }

        if (this !is FunctionExpr)
            return this.evaluate()

        val newArgs = args.map { if (hasProperty(FunctionExpr.Properties.HOLD)) it else it.eval() }.toMutableList()

        // Evaluate precision on sibling elements, numbers already handled but its possible that could be done here as well, need to try that
        if (size > 0) {
            val minPrecision = newArgs.minBy { it.precision }!!.precision
            for (i in 0 until newArgs.size) {
                if (newArgs[i] !is NumberExpr && minPrecision < newArgs[i].precision) {
                    val temp = newArgs[i].eval(minPrecision)
                    if (temp !is N)
                        newArgs[i] = temp
                }
            }
        }

        // Associative function, if the same function is a parameter move its parameters to the top level
        if (hasProperty(FunctionExpr.Properties.Flat)) {
            val same = newArgs.filter { it.javaClass == javaClass  }
            if (same.isNotEmpty()) {
                newArgs.removeIf { it.javaClass == javaClass }
                same.forEach { newArgs.addAll(it.args) }
            }
        }

        // Listable property
        if (hasProperty(FunctionExpr.Properties.LISTABLE) && size == 1 && newArgs[0] is ListExpr) {
            val listArgs = newArgs[0].args.map { FunctionExpr.createFunction(name, it) }
            return ListExpr(*listArgs.toTypedArray()).eval()
        }

        val function = FunctionExpr.createFunction(name, *newArgs.toTypedArray())
        try {
            (function as Expr).env = env
            function.validate()
            return function.evaluate()
        }
        catch (e: Exception) {
            return ErrorExpr(e.message!!)
        }
    }

    fun eval(precision: Int): Expr {
        return N(this, precision).eval()
    }

    fun print() {
        treeForm(0)
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

    protected fun indent(i: Int, s: String) {
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
        const val SYSTEM_DECIMAL_PRECISION = -1 // numbers evaluated to whatever primitive types hold
    }

    // Extensions for convenience
    fun toList(size: Int): ListExpr = ConstantArray(this, Integer(size)).eval().asList()

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (mArgs?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        return result
    }
}
