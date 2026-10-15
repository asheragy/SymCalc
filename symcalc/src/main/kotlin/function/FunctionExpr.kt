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

        val newArgs = args.map { if (has(Attribute.HoldAll)) it else it.eval(ctx) }.toMutableList()
        if (newArgs.any { it is Indeterminate })
            return Indeterminate()

        // Evaluate precision on sibling elements, numbers already handled but its possible that could be done here as well, need to try that
        if (size > 0) {
            val minPrecision = newArgs.minByOrNull { it.precision }!!.precision
            for (i in 0 until newArgs.size) {
                if (newArgs[i] !is NumberExpr && minPrecision < newArgs[i].precision) {
                    val temp = newArgs[i].eval(minPrecision)
                    if (temp !is N)
                        newArgs[i] = temp
                }
            }
        }

        // Associative function, if the same function is a parameter move its parameters to the top level
        if (has(Attribute.Flat)) {
            val same = newArgs.filter { it.javaClass == javaClass  }
            if (same.isNotEmpty()) {
                newArgs.removeIf { it.javaClass == javaClass }
                same.forEach { newArgs.addAll((it as FunctionExpr).args) }
            }
        }

        // Listable property
        if (has(Attribute.Listable) && newArgs.any { it is ListExpr }) {
            if (size == 1) {
                val listArgs = newArgs[0].asList().args.map { createFunction(name, it) }
                return ListExpr(*listArgs.toTypedArray()).eval(ctx)
            }
            else if (size == 2 && newArgs[0] is ListExpr || newArgs[1] is ListExpr) {
                val list1 = if(newArgs[0] is ListExpr) newArgs[0] as ListExpr else newArgs[0].toList(newArgs[1].asList().size)
                val list2 = if(newArgs[1] is ListExpr) newArgs[1] as ListExpr else newArgs[1].toList(newArgs[0].asList().size)

                if (list1.size != list2.size)
                    return ErrorExpr("lists of unequal lengths cannot be combined")

                val listArgs = list1.args.mapIndexed { index, expr -> createFunction(name, expr, list2[index]) }
                return ListExpr(*listArgs.toTypedArray()).eval(ctx)
            }
        }

        val function = createFunction(name, *newArgs.toTypedArray())
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
            ErrorExpr(e.message!!)
        }
    }

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
