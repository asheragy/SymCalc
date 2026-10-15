package org.cerion.symcalc.expression

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

internal class SymbolExprTest {

    @Test
    fun basic() {
        assertTrue(SymbolExpr("Sin").eval() is SymbolExpr)
        assertTrue(SymbolExpr("asdf").eval() is ErrorExpr)
    }
}
