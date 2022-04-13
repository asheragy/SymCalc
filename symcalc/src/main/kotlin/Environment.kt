package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr

import java.util.HashMap

class Environment {

    //Table of variables, x = expr
    private val vars = HashMap<String, Expr>()

    fun setVar(name: String, value: Expr) {
        vars[name] = value
    }

    fun getVar(name: String): Expr? {
        return vars[name]
    }

    companion object {


        //Table of function definitions
        //private val mFunctions: Map<String, UserFunction>? = null
    }

}
