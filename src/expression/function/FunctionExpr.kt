package org.cerion.symcalc.expression.function

import expression.SymbolExpr
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.MultiExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType

abstract class FunctionExpr protected constructor(vararg e: Expr) : MultiExpr(*e) {

    // TODO this is only needed to make copy, should be an easier way
    val name: String = this.javaClass.simpleName

    open val properties: Int get() = Properties.NONE.value

    val isNumeric: Boolean
        get() = hasProperty(Properties.NumericFunction)

    val symbol: SymbolExpr
        get() = SymbolExpr(name)

    override val type: ExprType
        get() = ExprType.FUNCTION

    override fun toString(): String {
        return name + args.toString()
    }

    @Throws(ValidationException::class)
    abstract fun validate()

    final override fun eval(): Expr {
        //https://reference.wolfram.com/language/tutorial/TheStandardEvaluationProcedure.html
        //https://reference.wolfram.com/language/tutorial/EvaluationOfExpressionsOverview.html

        // Set environment for every parameter to the current one before its evaluated
        for (i in 0 until size) {
            args[i].env = env
        }

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
                same.forEach { newArgs.addAll((it as FunctionExpr).args) }
            }
        }

        // Listable property
        if (hasProperty(FunctionExpr.Properties.LISTABLE) && newArgs.any { it is ListExpr }) {
            if (size == 1) {
                val listArgs = newArgs[0].asList().args.map { FunctionExpr.createFunction(name, it) }
                return ListExpr(*listArgs.toTypedArray()).eval()
            }
            else if (size == 2 && newArgs[0] is ListExpr || newArgs[1] is ListExpr) {
                val list1 = if(newArgs[0] is ListExpr) newArgs[0] as ListExpr else newArgs[0].toList(newArgs[1].asList().size)
                val list2 = if(newArgs[1] is ListExpr) newArgs[1] as ListExpr else newArgs[1].toList(newArgs[0].asList().size)

                if (list1.size != list2.size)
                    return ErrorExpr("lists of unequal lengths cannot be combined")

                val listArgs = list1.args.mapIndexed { index, expr -> FunctionExpr.createFunction(name, expr, list2[index]) }
                return ListExpr(*listArgs.toTypedArray()).eval()
            }
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

    override fun equals(e: Expr): Boolean {
        if (e !is FunctionExpr)
            return false

        if (this::class != e::class)
            return false

        if (e.size != size)
            return false

        if (hasProperty(Properties.Orderless)) {
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
    protected fun validateParameterRage(min: Int, max: Int) {
        if (size in min..max)
            return

        val error = "$name called with $size parameters, expected $min to $max"
        throw ValidationException(error)
    }

    fun hasProperty(attr: Properties): Boolean {
        val attrs = properties
        return attr.value and attrs != 0
    }

    // ssymb = Cases[Map[ToExpression, Names["System`*"]], _Symbol];
    // nfuns = Select[ssymb, MemberQ[Attributes[#], HoldFirst] &]
    enum class Properties constructor(val value: Int) {
        NONE(0),
        HOLD(1),
        LISTABLE(2),
        Flat(4),
        NumericFunction(8),
        Orderless(16)
    }

    companion object {
        @JvmStatic fun isValidFunction(functionName: String): Boolean = FunctionFactory.isValidFunction(functionName)
        @JvmStatic fun createFunction(f: String, vararg e: Expr): FunctionExpr = FunctionFactory.createInstance(f, *e)
    }
}