package org.cerion.symcalc.parser

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.function.FunctionExpr

//import java.util.Hashtable;


class Lexer(exprStr: String) {

    private val exprStr: String
    private var pos: Int = 0

    init {
        pos = -1
        //remove WhiteSpace
        this.exprStr = exprStr.replace(" ".toRegex(), "")

    }

    var tokval: String = ""
    val token: Char
        get() {

            pos++
            if (pos >= exprStr.length)
                return 0.toChar()

            val c = exprStr[pos]

            when (c) {
                '+', '-', '*', '/', 'i', '(', ')', '[', ']', '{', '}', '^', ',', '!' -> return c

                else -> {
                    if (Character.isDigit(c) || c == '.') {
                        tokval = Character.toString(c)

                        while (hasInput() && (Character.isDigit(exprStr[pos + 1]) || exprStr[pos + 1] == '.' || exprStr[pos + 1] == 'i')) {
                            pos++
                            tokval += exprStr[pos]
                        }
                        return Parser.NUMBER
                    }
                    if (Character.isLetter(c)) {
                        tokval = Character.toString(c)
                        while (hasInput() && Character.isLetter(exprStr[pos + 1])) {
                            pos++
                            tokval += exprStr[pos]
                        }
                        if (isIdent(tokval)) {
                            tokval = tokval.toLowerCase()
                            return 'f'
                        } else if (ConstExpr.isConstant(tokval)) {
                            tokval = tokval.toLowerCase()
                            return 'c'
                        }

                        tokval = tokval.toLowerCase()
                        return 'v'
                    }
                }
            }

            tokval = "error"
            return 'e'
        }

    fun hasInput(): Boolean {
        return (pos + 1 < exprStr.length)
    }

    private fun isIdent(s: String): Boolean {
        return FunctionExpr.isValidFunction(s)
    }
}
