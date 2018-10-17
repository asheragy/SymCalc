package org.cerion.symcalc.expression

import org.cerion.symcalc.Environment
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.parser.Lexer
import org.cerion.symcalc.parser.Parser

import java.util.ArrayList

abstract class Expr {

    var value: Any? = null
        protected set

    var env = Environment()
        private set

    private var mArgs: MutableList<Expr>? = null
    private val args: List<Expr>?
        get() = mArgs

    protected fun getEnvVar(name: String): Expr? = env.getVar(name)
    protected fun setEnvVar(name: String, e: Expr) = env.setVar(name, e)

    val all: List<Expr>?
        get() {
            if (!hasProperty(Properties.HOLD) && !env.skipEval) {
                val args = ArrayList<Expr>()
                for (i in 0 until size())
                    args.add(get(i).eval())

                return args
            }

            return mArgs
        }

    abstract val type: ExprType

    protected open val properties: Int
        get() = Properties.NONE.value

    val isNumber: Boolean
        get() = type == ExprType.NUMBER

    open val isInteger: Boolean
        get() = isNumber && asInteger().isInteger

    val isList: Boolean
        get() = type == ExprType.LIST

    val isFunction: Boolean
        get() = type == ExprType.FUNCTION

    val isError: Boolean
        get() = type == ExprType.ERROR

    val isVariable: Boolean
        get() = type == ExprType.VARIABLE

    val isBool: Boolean
        get() = type == ExprType.BOOL

    val isConst: Boolean
        get() = type == ExprType.CONST

    protected fun setArgs(vararg args: Expr) {
        if (mArgs == null)
            mArgs = ArrayList()

        for (k in args) {
            mArgs!!.add(k)
        }
    }

    fun args(): Array<Expr> {
        return mArgs!!.toTypedArray()
    }

    // Indexes
    operator fun get(index: Int): Expr {
        return if (!hasProperty(Properties.HOLD) && !env.skipEval) {
            mArgs!![index].eval()
        } else mArgs!![index]

    }

    operator fun set(index: Int, e: Expr) {
        mArgs!![index] = e
    }

    fun getList(index: Int): ListExpr = get(index) as ListExpr
    fun getInteger(index: Int): IntegerNum = get(index) as IntegerNum

    // Casting
    fun asList(): ListExpr = this as ListExpr
    fun asInteger(): IntegerNum = this as IntegerNum
    fun asReal(): RealNum = this as RealNum
    fun asBool(): BoolExpr = this as BoolExpr
    fun asVar(): VarExpr = this as VarExpr

    protected fun setArg(index: Int, e: Expr) {
        if (mArgs == null)
            mArgs = ArrayList()

        if (index == mArgs!!.size)
            mArgs!!.add(e)
        else
            mArgs!![index] = e
    }

    protected fun addArg(e: Expr) {
        setArg(size(), e)
    }

    fun size(): Int = if (mArgs != null) mArgs!!.size else 0

    protected fun argString(): String {
        return mArgs!!.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Expr) {
            return this.equals(other)
        }

        return false
    }

    abstract override fun toString(): String
    abstract fun show(i: Int)  //rename to something like treeForm or make different function, can probably make non-abstract if types are standardized (name + value/args)
    abstract fun equals(e: Expr): Boolean
    protected abstract fun evaluate(): Expr

    fun eval(): Expr {
        //https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
        //https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

        // Associative function, if the same function is a parameter move its parameters to the top level
        if (hasProperty(Properties.ASSOCIATIVE)) {
            var i = 0
            while (i < size()) {
                if (get(i).javaClass == javaClass) {
                    // insert these sub parameters at the same position it was removed
                    val t = mArgs!![i]
                    mArgs!!.removeAt(i)
                    mArgs!!.addAll(i, t.args!!)
                    i--
                }
                i++
            }
        }

        env.skipEval = false

        if (isFunction) {
            try {
                (this as FunctionExpr).validate()
            } catch (e: ValidationException) {
                return ErrorExpr(e.message!!)
            }

        }

        // Set environment for every parameter to the current one before its evaluated
        for (i in 0 until size()) {
            mArgs!![i].env = env
        }

        // Skip eval for Hold property
        /*
		if(!hasProperty(Properties.HOLD)) {
			for (int i = 0; i < size(); i++) {
				setArg(i, get(i).eval());

				//Return the first error
				if (get(i).isError())
					return get(i);
			}
		}
		*/

        // Listable property
        if (hasProperty(Properties.LISTABLE) && isFunction) {
            val function = this as FunctionExpr
            if (size() == 1 && get(0).isList) {
                val p1 = get(0) as ListExpr
                val result = ListExpr()

                for (i in 0 until p1.size())
                    result.add(FunctionExpr.createFunction(function.name, p1[i]))

                return result.eval()
            }
        }

        return try {
            evaluate()
        } catch (e: Exception) {
            ErrorExpr(e.toString())
        }
    }

    fun print() {
        show(0)
    }

    enum class ExprType {
        NUMBER,
        VARIABLE,
        FUNCTION,
        LIST,
        CONST,
        ERROR,
        BOOL,
        GRAPHICS
    }

    enum class Properties constructor(val value: Int) {
        NONE(0),
        HOLD(1),
        LISTABLE(2),
        ASSOCIATIVE(4),
        CONSTANT(8),
        NumericFunction(16)
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

    //TODO, may be an actual function that does this
    fun toList(size: Int): ListExpr {
        val result = ListExpr()
        for (i in 0 until size)
            result.add(this)

        return result
    }

    companion object {
        @JvmStatic
        fun parse(s: String): Expr {
            val lex = Lexer(s)
            val p = Parser(lex)
            return p.e
        }
    }
}
