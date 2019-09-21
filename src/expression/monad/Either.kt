package org.cerion.symcalc.expression.monad

import org.cerion.symcalc.expression.Expr

sealed class Either<out A : Expr, out B: Expr> : Expr() {

    private val value: Expr
    get() {
        if (isLeft)
            return (this as Left).a
        else
            return (this as Right).b
    }

    abstract val isRight: Boolean
    abstract val isLeft: Boolean

    override val type: ExprType
        get() = value.type

    override fun toString(): String = value.toString()
    override fun equals(e: Expr): Boolean = value.equals(e)
    override fun eval(): Expr = value.eval()

    data class Left<out A : Expr>(val a: A) : Either<A, Nothing>() {
        override val isRight: Boolean get() = false
        override val isLeft: Boolean get() = true

        companion object {
            operator fun <A: Expr> invoke(a: A): Either<A, Nothing> = Left(a)
        }
    }

    data class Right<out B : Expr>(val b: B) : Either<Nothing, B>() {
        override val isRight: Boolean get() = true
        override val isLeft: Boolean get() = false
    }

    companion object {
        fun <L : Expr>left(value: L) : Either<L, Nothing> = Left(value)
        fun <R : Expr>right(value: R) : Either<Nothing, R> = Right(value)
    }
}