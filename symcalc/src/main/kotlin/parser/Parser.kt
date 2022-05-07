package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.integer.Factorial
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

class ParseException(message: String?) : Exception(message)

class Parser(private val lex: Lexer, start: Expr? = null) {

    var e: Expr

    private var token: Char = ' '
    private var tokval: String = ""

    init {
        if (start != null) {
            token = '%'
            e = start
        }
        else
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
	       | expr + expr2
	       | expr - expr2

	 expr2 = expr3
	       | term * expr3
	       | term / expr3

     expr3 = factor
           | expr ^ factor

     expr4 = factor
           | expr!

	factor = integer
	       |  x
	       | - expr
	       | foo(expr)
	       | (expr)
	       | % - Value passed in for start
	*/
    private fun expr(): Expr {
        var e = expr2()

        while (true) {
            e = when(token) {
                '+' -> {
                    getNext()
                    Plus(e, expr2())
                }
                '-' -> {
                    getNext()
                    Subtract(e, expr2())
                }
                else -> return e
            }
        }
    }

    private fun expr2(): Expr {
        var e = expr3()

        while (true) {
            e = when (token) {
                '*' -> {
                    getNext()
                    Times(e, expr3())
                }
                '/' -> {
                    getNext()
                    Divide(e, expr3())
                }
                else -> return e
            }
        }
    }

    private fun expr3(): Expr {
        var e = expr4()

        while (true) {
            e = when(token) {
                '^' -> {
                    getNext()
                    Power(e, expr4())
                }
                else -> return e
            }
        }
    }

    private fun expr4(): Expr {
        var e = factor()

        while (true) {
            e = when(token) {
                '!' -> {
                    getNext()
                    Factorial(e)
                }
                else -> return e
            }
        }
    }

    private fun factor(): Expr {
        when (token) {
            NUMBER -> {
                getNext()
                return NumberExpr.parse(tokval)
            }
            //Complex 1
            'i' -> {
                getNext()
                return NumberExpr.parse("i")
            }
            // Variable
            'v' -> {
                getNext()
                return VarExpr(tokval)
            }
            // Initial input is passed in Expr
            '%' -> {
                getNext()
                return e
            }
            // Negate
            '-' -> {
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
            '{' -> {
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
            FUNC -> {
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

            }
            CONST -> {
                getNext()
                return ConstExpr.getConstant(tokval)
            }
            '(' -> {
                getNext()
                val e = expr()
                if (token == ')') {
                    getNext()
                    return e
                }
                else
                    throw ParseException("Missing closing ')'")
            }
            else -> throw ParseException("Incomplete expression")
        }
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
