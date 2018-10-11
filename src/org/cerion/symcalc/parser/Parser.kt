package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.NumberExpr

class Parser(private val lex: Lexer) {

    var e: Expr?

    private var token: Char = ' '
    private var tokval: String? = null

    init {
        getNext() //start input
        this.e = expr()
    }

    /*
	  expr = factor
	       | expr + term
	       | expr - term

	  term = expterm
	       | term * expterm
	       | term / expterm

   expterm = factor
           | expterm ^ factor

	factor = integer
	       |  x
	       | - expr
	       | expr!
	       | foo(expr)
	       | (expr)
	*/
    private fun expr(): Expr? {

        var e = term()

        while (lex.hasInput()) {

            when(token) {
                '+' -> {
                    getNext()
                    e = Plus(e, term())
                }
                '-' -> {
                    getNext()
                    e = Subtract(e, term())
                }
                else -> return e
            }
        }

        return e
    }

    private fun term(): Expr? {
        var e = expterm()

        while (lex.hasInput()) {
            if (token == '*') {
                getNext()
                e = Times(e, expterm())
            } else if (token == '/') {
                getNext()
                e = Divide(e, expterm())
            } else {
                return e
            }
        }

        return e
    }

    private fun expterm(): Expr? {
        var e = factor()

        while (lex.hasInput()) {
            if (token == '^') {
                getNext()
                e = Power(e, factor())
            } else {
                return e
            }
        }


        return e
    }

    private fun factor(): Expr? {

        if (token == NUMBER) {
            //if (lex.hasInput())
            run {
                getNext()

            }
            //Number f = new Number(tokval);
            return NumberExpr.parse(tokval!!)
        } else if (token == 'i')
        //complex 1
        {
            getNext()
            return NumberExpr.parse("i")
        } else if (token == 'v') {
            //if(lex.hasInput())
            getNext()
            return VarExpr(tokval!!)
        } else if (token == '-') {
            getNext()
            var next = factor()
            //if number just negate it
            if (next!!.isNumber)
                next = (next as NumberExpr).negate()
            else
                next = Times(IntegerNum("-1"), next)

            return next
        } else if (token == '{') {
            getNext()
            val ml = ListExpr(expr()!!)

            while (token == ',') {
                getNext()
                ml.add(expr()!!)
            }
            if (token != '}')
                println("Edit: missing }")
            getNext()
            return ml
        } else if (token == FUNC) {
            val ident = tokval
            getNext()
            if (token != '(')
                println("missing ident (")
            getNext()

            val mf = FunctionExpr.createFunction(ident!!, expr()!!)

            while (token == ',')
            //Add additional parameters
            {
                getNext()
                mf!!.add(expr()!!)
            }

            if (token == ')') {
                getNext()
                return mf
            } else
                println("missing ident )")

        } else if (token == CONST) {
            getNext()
            return ConstExpr.getConstant(tokval!!)
        } else if (token == '(') {

            getNext()
            val e = expr()
            if (token == ')') {
                getNext()
                return e
            } else {
                println("missing ')'")
            }
            return e

        } else {

            println("syntax error")
        }

        println("error: return null")
        return null /* not used */
    }

    private fun getNext() {
        token = lex.token
        tokval = lex.tokval
    }

    companion object {
        const val NUMBER = '0'
        const val CONST = 'c'
        const val FUNC = 'f'
    }
}
