package org.cerion.symcalc.expression

import expression.function.list.ConstantArray
import org.cerion.symcalc.Environment
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.parser.Lexer
import org.cerion.symcalc.parser.Parser
import java.util.*

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
    protected open val properties: Int get() = Properties.NONE.value

    val size get() = if (mArgs != null) mArgs!!.size else 0

    val isNumber: Boolean get() = type == ExprType.NUMBER
    open val isInteger: Boolean get() = isNumber && asInteger().isInteger
    val isList: Boolean get() = type == ExprType.LIST
    val isFunction: Boolean get() = type == ExprType.FUNCTION
    val isError: Boolean get() = type == ExprType.ERROR
    val isVariable: Boolean get() = type == ExprType.VARIABLE
    val isBool: Boolean get() = type == ExprType.BOOL
    val isConst: Boolean get() = type == ExprType.CONST

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
    //fun asReal(): RealNum = this as RealNum
    fun asBool(): BoolExpr = this as BoolExpr
    fun asVar(): VarExpr = this as VarExpr
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
        //https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
        //https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

        // Set environment for every parameter to the current one before its evaluated
        for (i in 0 until size) {
            args[i].env = env
        }

        // Make a copy of this expression to evaluate and return
        var result = this
        if (this is FunctionExpr)
            result = FunctionExpr.createFunction(name)
        else if (this is ListExpr)
            result = ListExpr()

        if (this is FunctionExpr || this is ListExpr) {
            result.env = env
            for (arg in args) {
                if (hasProperty(Properties.HOLD))
                    result.addArg(arg)
                else
                    result.addArg(arg.eval())
            }

            // Evaluate precision on sibling elements, numbers already handled but its possible that could be done here as well, need to try that
            if (size > 0) {
                val minPrecision = result.args.minBy { it.precision }!!.precision
                for (i in 0 until result.args.size) {
                    if (result.args[i] !is NumberExpr && minPrecision < result.args[i].precision) {
                        val temp = result.args[i].eval(minPrecision)
                        if (temp !is N)
                            result.mArgs!![i] = temp
                    }
                }
            }
        }

        // Associative function, if the same function is a parameter move its parameters to the top level
        if (hasProperty(Properties.ASSOCIATIVE)) {
            var i = 0
            while (i < size) {
                if (result[i].javaClass == javaClass) {
                    // insert these sub parameters at the same position it was removed
                    val t = result.mArgs!![i]
                    result.mArgs!!.removeAt(i)
                    result.mArgs!!.addAll(i, t.args)
                    i--
                }
                i++
            }
        }

        // Listable property
        if (hasProperty(Properties.LISTABLE) && size == 1 && result[0].isList) {
            val function = result as FunctionExpr
            val p1 = result[0] as ListExpr
            val listResult = ListExpr()

            for (i in 0 until p1.size)
                listResult.add(FunctionExpr.createFunction(function.name, p1[i]))

            return listResult.eval()
        }

        return try {
            if (result is FunctionExpr)
                result.validate()

            result.evaluate()
        }
        catch (e: Exception) {
            ErrorExpr(e.message!!)
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

    // ssymb = Cases[Map[ToExpression, Names["System`*"]], _Symbol];
    // nfuns = Select[ssymb, MemberQ[Attributes[#], HoldFirst] &]
    enum class Properties constructor(val value: Int) {
        NONE(0),
        HOLD(1),
        LISTABLE(2),
        ASSOCIATIVE(4),
        CONSTANT(8),
        NumericFunction(16),
        Orderless(32)
    }

    protected enum class LogicalCompare {
        TRUE,
        FALSE,
        UNKNOWN,
        ERROR
    }

    protected fun hasProperty(attr: Properties): Boolean {
        val attrs = properties
        return attr.value and attrs != 0
    }

    protected fun indent(i: Int, s: String) {
        for (ii in 0 until 2 * i)
            print(" ")
        println(s)
    }

    fun isFunction(name: String): Boolean {
        if (isFunction) {
            val e = this as FunctionExpr
            if (name.compareTo(e.name, ignoreCase = true) == 0)
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
