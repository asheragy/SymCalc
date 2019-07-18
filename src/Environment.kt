package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr

import java.util.HashMap

class Environment {

    //Table of variables, x = expr
    private val vars = HashMap<String, Expr>()

    var skipEval = true

    //Decimal evaluation precision
    private var evalNumber = false
    var precision = SYSTEM_DECIMAL_PRECISION
        private set

    var isNumericalEval: Boolean
        get() = evalNumber
        set(bEval) = setNumericalEval(bEval, SYSTEM_DECIMAL_PRECISION)

    fun setVar(name: String, value: Expr) {
        vars[name] = value
    }

    fun getVar(name: String): Expr? {
        return vars[name]
    }

    fun setNumericalEval(bEval: Boolean, digits: Int) {
        evalNumber = bEval
        precision = if (digits >= 0) digits else SYSTEM_DECIMAL_PRECISION
    }

    companion object {
        const val SYSTEM_DECIMAL_PRECISION = -1 // numbers evaluated to whatever primitive types hold

        //Table of function definitions
        //private val mFunctions: Map<String, UserFunction>? = null
    }

}
