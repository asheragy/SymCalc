package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.function.arithmetic.Times
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class IntegerNum(override val value: BigInteger) : NumberExpr() {
    companion object {
        @JvmField val ZERO = IntegerNum(0)
        @JvmField val ONE = IntegerNum(1)
        @JvmField val TWO = IntegerNum(2)
        @JvmField val NEGATIVE_ONE = IntegerNum(-1)
        val MAX_INT = IntegerNum(Int.MAX_VALUE)
        val MIN_INT = IntegerNum(Int.MIN_VALUE)
    }

    override val isZero: Boolean get() = value == BigInteger.ZERO
    override val isOne: Boolean get() = value == BigInteger.ONE
    override val numType: NumberType get() = NumberType.INTEGER
    override val isNegative: Boolean get() = signum == -1
    override val precision: Int get() = InfinitePrecision

    val isEven: Boolean get() = !value.testBit(0)
    val isOdd: Boolean get() = value.testBit(0)
    val signum: Int get() = value.signum()

    constructor(s: String) : this(BigInteger(s))
    constructor(n: Long) : this(BigInteger.valueOf(n))
    constructor(n: Int) : this(BigInteger.valueOf(n.toLong()))

    override fun toString(): String = value.toString()
    override fun toDouble(): Double = value.toDouble()

    override fun evaluate(precision: Int): NumberExpr {
        return when (precision) {
            InfinitePrecision -> this
            SYSTEM_DECIMAL_PRECISION -> RealNum.create(this)
            else -> {
                //if (isZero)
                //    return RealNum.create(0.0) // Prevents lost precision for BigDecimal(0)

                var bd = BigDecimal(value, MathContext(precision, RoundingMode.HALF_UP))
                bd = bd.setScale(precision)
                return RealNum_BigDecimal(bd)
            }

        }
    }

    fun intValue(): Int {
        if (this > MAX_INT || this < MIN_INT)
            throw OperationException("int value is out of range")

        return value.toInt()
    }

    fun toBigInteger(): BigInteger = value
    fun toBigDecimal(): BigDecimal = BigDecimal(value)

    operator fun plus(N: IntegerNum): IntegerNum = IntegerNum(value.add(N.value))
    operator fun minus(n: IntegerNum): IntegerNum = IntegerNum(value.subtract(n.value))
    operator fun times(n: IntegerNum): IntegerNum = IntegerNum(value.multiply(n.value))
    override fun unaryMinus(): IntegerNum = IntegerNum(value.negate())

    override fun plus(other: NumberExpr): NumberExpr {
        return if (other.isInteger) plus(other.asInteger()) else other.plus(this)
    }

    override fun times(other: NumberExpr): NumberExpr {
        return if (other.isInteger) times(other.asInteger()) else other.times(this)
    }

    override fun div(other: NumberExpr): NumberExpr {
        //Any code calling this should check
        if (other.isZero)
            throw ArithmeticException("divide by zero")

        when (other.numType) {
            NumberType.INTEGER -> {
                val n = other as IntegerNum
                val gcd = this.gcd(n)

                if (gcd.isOne) {
                    return Rational(this, n)
                }

                //Divide both by GCD
                val a = IntegerNum(value.divide(gcd.value))
                val b = IntegerNum(n.value.divide(gcd.value))

                if (b.isOne)
                    return a

                return Rational(a, b)
            }
            NumberType.RATIONAL -> {
                other as Rational
                return Times(this, Rational(other.denominator, other.numerator)).eval() as NumberExpr
            }

            NumberType.REAL -> {
                return evaluate(other.precision) / other
            }

            NumberType.COMPLEX -> return Complex(this) / other
        }
    }

    //IntegerNum Specific Functions
    fun gcd(N: IntegerNum): IntegerNum = IntegerNum(value.gcd(N.value))

    fun powerMod(b: IntegerNum, m: IntegerNum): IntegerNum {
        //Assuming all integers at this point since MathFunc needs to check that
        val num = value
        val exp = b.value
        val mod = m.value

        return IntegerNum(num.modPow(exp, mod))
    }

    fun primeQ(): Boolean = value.isProbablePrime(5)

    operator fun inc(): IntegerNum = IntegerNum(value.inc())
    operator fun dec(): IntegerNum = IntegerNum(value.minus(BigInteger.ONE))
    operator fun rem(N: IntegerNum): IntegerNum = IntegerNum(value.mod(N.value))

    override fun compareTo(other: NumberExpr): Int {
        return when (other.numType) {
            NumberType.INTEGER -> value.compareTo(other.asInteger().value)
            NumberType.RATIONAL -> this.toDouble().compareTo(other.toDouble())
            NumberType.REAL -> RealNum.create(this).compareTo(other)
            NumberType.COMPLEX -> Complex(this).compareTo(other)
        }
    }

    fun pow(other: IntegerNum): NumberExpr {
        val intVal = other.asInteger().value.toInt()
        if (intVal < 0)
            return Rational(ONE, IntegerNum(value.pow(-intVal))).evaluate()
        else
            return IntegerNum(value.pow(intVal))
    }
}
