package org.cerion.symcalc.function

import org.cerion.symcalc.EvalContext
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.*
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.NumberType

abstract class FunctionExpr (vararg e: Any) : MultiExpr(convertArgs(*e))
{
    // TODO this is only needed to make copy, should be an easier way
    val name: String = this.javaClass.simpleName

    open val attributes: Set<Attribute> get() = emptySet()
    protected fun has(a: Attribute) = a in attributes

    val isNumeric: Boolean
        get() = has(Attribute.NumericFunction)

    val symbol: SymbolExpr
        get() = SymbolExpr(name)

    override val type: ExprType
        get() = ExprType.FUNCTION

    override fun toString(): String {
        return name + args.toList().toString()
    }

    override fun toLatex(): String {
        return name + args.map { it.toLatex() }.toList().toString()
    }

    @Throws(ValidationException::class)
    open fun validate() {
        validateParameterRange(1, Int.MAX_VALUE) // By default all functions take at least 1 parameter
    }

    /* Might be useful sometime
    operator fun invoke(vararg e: Expr): Expr {
        return createFunction(name, *e).eval()
    }
     */

    protected abstract fun evaluate(): Expr

    protected lateinit var ctx: EvalContext

    final override fun eval(ctx: EvalContext): Expr {
        this.ctx = ctx
        val start = System.currentTimeMillis()

        //https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
        //https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

        var newArgs = args.mapIndexed { index, arg ->
            if (has(Attribute.HoldAll) || (index == 0 && has(Attribute.HoldFirst)) || (index != 0 && has(Attribute.HoldRest)))
                arg
            // arg is Evaluate && !has(Attribute.HoldAllComplete) -> arg[0].eval(ctx)   // Evaluate[] overrides a hold
            else
                arg.eval(ctx)
        }

        if (newArgs.any { it is Indeterminate }) return Indeterminate()
        newArgs = normalisePrecision(newArgs)

        // Sequence splicing, unless SequenceHold / HoldAllComplete.
        //if (!has(Attribute.SequenceHold) && !has(Attribute.HoldAllComplete))
        //    newArgs = newArgs.flatMap { if (it is Sequence) it.args.toList() else listOf(it) }

        if (has(Attribute.Flat))
            newArgs = newArgs.flatMap { if (it.javaClass == javaClass) (it as FunctionExpr).args.toList() else listOf(it) }
        if (has(Attribute.Listable))
            threadOverLists(newArgs)?.let { return it.eval(ctx) }
        if (has(Attribute.Orderless))
            newArgs = canonicalSort(newArgs)

        val function = copy(newArgs)
        function.ctx = ctx

        return try {
            function.validate()
            val result = function.evaluate()
            val end = System.currentTimeMillis()
            val diff = end - start
            //if (diff > 16)
            //    println("$diff $this")

            return result
        }
        catch (e: Exception) {
            ErrorExpr(e.message ?: "Unknown error")
        }
    }

    // Evaluate precision on sibling elements, numbers already handled but its possible that could be done here as well, need to try that
    private fun normalisePrecision(args: List<Expr>): List<Expr> {
        if (size == 0) return args

        val minPrecision = args.minByOrNull { it.precision }!!.precision
        return args.map {
            if (it !is NumberExpr && minPrecision < it.precision) {
                val temp = it.eval(minPrecision)
                if (temp !is N)
                    temp
                else
                    it
            }
            else
                it
        }
    }

    private fun canonicalSort(args: List<Expr>): List<Expr> {
        // TODO need to define what this sort actually does
        return args
    }

    private fun threadOverLists(args: List<Expr>): Expr? {
        val lists = args.filterIsInstance<ListExpr>()
        if (lists.isEmpty())
            return null

        val length = lists[0].size
        if (lists.any { it.size != length })
            return ErrorExpr("lists of unequal lengths cannot be combined")

        // One call per index: list arguments contribute their i-th element, everything else is repeated.
        val calls = (0 until length).map { i ->
            copy(args.map { if (it is ListExpr) it[i] else it })
        }
        return ListExpr(*calls.toTypedArray())
    }

    /** Same head, new arguments. Built-ins go through the factory; user calls keep their head name. */
    protected open fun copy(newArgs: List<Expr>): FunctionExpr =
        FunctionFactory.createInstance(name, *newArgs.toTypedArray())

    override fun equals(e: Expr): Boolean {
        if (e !is FunctionExpr)
            return false

        if (this::class != e::class)
            return false

        if (e.size != size)
            return false

        if (has(Attribute.Orderless)) {
            val otherArgs = e.args.toMutableList()
            for (arg in args) {
                for(i in 0 until otherArgs.size)
                    if (arg == otherArgs[i]) {
                        otherArgs.removeAt(i)
                        break
                    }
            }

            return otherArgs.size == 0
        }
        else {
            for (i in 0 until size) {
                if (!get(i).equals(e[i]))
                    return false
            }
        }

        return true
    }

    @Throws(ValidationException::class)
    protected fun validateParameterType(position: Int, type: ExprType) {
        if (size < position+1)
            validateParameterCount(position+1)

        if (size < position || get(position).type != type)
            throw ValidationException(String.format("parameter %d must be type %s", position, type))
    }

    @Throws(ValidationException::class)
    protected fun validateNumberType(position: Int, numberType: NumberType) {
        if (size < position+1)
            validateParameterCount(position+1)

        val e = get(position) as NumberExpr
        if (e.numType != numberType)
            throw ValidationException(String.format("parameter %d must be type %s", position, numberType))
    }

    @Throws(ValidationException::class)
    protected fun validateParameterCount(expected: Int) {
        if (size == expected)
            return

        val error = String.format("%d parameters, expected %d", size, expected)
        throw ValidationException(error)
    }

    @Throws(ValidationException::class)
    protected fun validateParameterRange(min: Int, max: Int) {
        if (size in min..max)
            return

        val error = "$name called with $size parameters, expected $min to $max"
        throw ValidationException(error)
    }

    companion object {
        @JvmStatic fun isValidFunction(functionName: String): Boolean = FunctionFactory.isValidFunction(functionName)
        @JvmStatic fun createFunction(f: String, vararg e: Expr): FunctionExpr = FunctionFactory.createInstance(f, *e)
    }
}
