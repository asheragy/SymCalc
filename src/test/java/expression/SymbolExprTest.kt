package expression

import org.cerion.symcalc.expression.ErrorExpr
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SymbolExprTest {

    @Test
    fun basic() {
        assertTrue(SymbolExpr("sin").eval() is SymbolExpr)
        assertTrue(SymbolExpr("asdf").eval() is ErrorExpr)
    }
}