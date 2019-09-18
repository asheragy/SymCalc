package org.cerion.symcalc.expression.monad

import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.expression.Expr

sealed class Either<out A : Expr, out B: Expr>(override val value: Expr) : Expr(), AtomExpr {

    abstract val isRight: Boolean
    abstract val isLeft: Boolean

    override val type: ExprType
        get() = value.type

    override fun treeForm(i: Int) {
        value.treeForm(i)
    }

    override fun toString(): String = value.toString()
    override fun equals(e: Expr): Boolean = value.equals(e)
    override fun evaluate(): Expr = value.eval()

    data class Left<out A : Expr>(override val value: A) : Either<A, Nothing>(value) {
        val a: A = value
        override val isRight: Boolean get() = false
        override val isLeft: Boolean get() = true

        companion object {
            operator fun <A: Expr> invoke(a: A): Either<A, Nothing> = Left(a)
        }
    }

    data class Right<out B : Expr>(override val value: B) : Either<Nothing, B>(value) {
        val b: B = value
        override val isRight: Boolean get() = false
        override val isLeft: Boolean get() = true
    }

    companion object {
        fun <L : Expr>left(value: L) : Either<L, Nothing> = Left(value)
        fun <R : Expr>right(value: R) : Either<Nothing, R> = Right(value)
    }
}