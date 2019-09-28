package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr

class ParseException(message: String?) : Exception(message)

class Parser(private val lex: Lexer) {

    var e: Expr

    private var token: Char = ' '
    private var tokval: String = ""

    init {
        getNext() //start input

        try {
            this.e = expr()
            if (token != 0.toChar())
                throw ParseException("Unexpected input $token")
        }
        catch(ex: Exception) {
            this.e = ErrorExpr(ex.toString())
        }
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
    private fun expr(): Expr {
        var e = term()

        while (true) {
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
    }

    private fun term(): Expr {
        var e = expterm()

        while (true) {
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
    }

    private fun expterm(): Expr {
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

    private fun factor(): Expr {

        if (token == NUMBER) {
            getNext()
            return NumberExpr.parse(tokval)
        }
        //Complex 1
        else if (token == 'i') {
            getNext()
            return NumberExpr.parse("i")
        }
        // Variable
        else if (token == 'v') {
            //if(lex.hasInput())
            getNext()
            return VarExpr(tokval)
        }
        // Negate
        else if (token == '-') {
            getNext()
            var next = factor()
            //if number just negate it
            if (next is NumberExpr)
                next = next.unaryMinus()
            else
                next = Times(Integer("-1"), next)

            return next
        }
        // Lists
        else if (token == '{') {
            getNext()
            val items = mutableListOf(expr())

            while (token == ',') {
                getNext()
                items.add(expr())
            }
            if (token != '}')
                println("Edit: missing }")
            getNext()
            return ListExpr(items)
        }
        // Function
        else if (token == FUNC) {
            val ident = tokval
            getNext()
            if (token != '(')
                println("missing ident (")

            getNext()
            val functionArgs = mutableListOf(expr())

            //Add additional parameters
            while (token == ',')
            {
                getNext()
                functionArgs.add(expr())
            }

            if (token == ')') {
                getNext()
                return FunctionExpr.createFunction(ident, *functionArgs.toTypedArray())
            } else
                throw ParseException("Function missing closing ')'")

        } else if (token == CONST) {
            getNext()
            return ConstExpr.getConstant(tokval)
        } else if (token == '(') {
            getNext()
            val e = expr()
            if (token == ')') {
                getNext()
                return e
            }
            else
                throw ParseException("Missing closing ')'")
        }
        else
            throw ParseException("Incomplete expression")
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
