package org.cerion.symcalc.expression.function

import expression.SymbolExpr
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType

abstract class FunctionExpr protected constructor(final override val value: Function, vararg e: Expr) : Expr(*e) {
    val name: String = value.toString()

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

    override fun treeForm(i: Int) {
        indent(i, "$name[]")
        for (j in 0 until size)
            get(j).treeForm(i + 1)
    }

    @Throws(ValidationException::class)
    abstract fun validate()

    override fun equals(e: Expr): Boolean {
        if (e !is FunctionExpr)
            return false

        if (e.value != value)
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