package org.cerion.symcalc.expression.function

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType

abstract class FunctionExpr protected constructor(val function: Function, vararg val e: Expr) : Expr() {
    val name: String = function.toString()

    val isNumeric: Boolean
        get() = hasProperty(Properties.NumericFunction)

    init {
        setArgs(*e)
    }

    override val type: ExprType
        get() = ExprType.FUNCTION

    override fun toString(): String {
        return name + argString()
    }

    override fun treeForm(i: Int) {
        indent(i, "$name[]")
        for (j in 0 until size)
            get(j).treeForm(i + 1)
    }

    @Throws(ValidationException::class)
    abstract fun validate()

    override fun equals(e: Expr): Boolean {
        if (!e.isFunction)
            return false

        val f = e as FunctionExpr
        if (f.function != function)
            return false

        if (f.size != size)
            return false

        for (i in 0 until size) {
            if (!get(i).equals(f[i]))
                return false
        }

        return true
    }

    fun add(t: Expr) {
        setArgs(t)
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

    companion object {
        @JvmStatic fun isValidFunction(functionName: String): Boolean = FunctionFactory.isValidFunction(functionName)
        @JvmStatic fun createFunction(f: String, vararg e: Expr): FunctionExpr = FunctionFactory.createInstance(f, *e)
    }
}