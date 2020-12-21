package org.cerion.symcalc.expression.number

import org.cerion.symcalc.constant.I
import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.integer.Factor
import org.cerion.symcalc.function.list.Tally
import org.nevec.rjm.BigIntegerMath
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class Integer(override val value: BigInteger) : NumberExpr(), AtomExpr {
    companion object {
        @JvmField val ZERO = Integer(0)
        @JvmField val ONE = Integer(1)
        @JvmField val TWO = Integer(2)
        @JvmField val NEGATIVE_ONE = Integer(-1)
        val MAX_INT = Integer(Int.MAX_VALUE)
        val MIN_INT = Integer(Int.MIN_VALUE)
    }

    override val type: ExprType get() = ExprType.NUMBER
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

    override fun toPrecision(precision: Int): NumberExpr {
        return when (precision) {
            InfinitePrecision -> this
            MachinePrecision -> RealDouble(value.toDouble())
            else -> {
                var bd = BigDecimal(value, MathContext(precision, RoundingMode.HALF_UP))
                if (precision > bd.precision())
                    bd = bd.setScale(precision - bd.precision())
                return RealBigDec(bd)
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

    operator fun plus(N: Integer): Integer = Integer(value.add(N.value))
    operator fun minus(n: Integer): Integer = Integer(value.subtract(n.value))
    operator fun times(n: Integer): Integer = Integer(value.multiply(n.value))
    override fun unaryMinus(): Integer = Integer(value.negate())

    override fun plus(other: NumberExpr): NumberExpr {
        return if (other is Integer) plus(other) else other.plus(this)
    }

    override fun times(other: NumberExpr): NumberExpr {
        return if (other is Integer) times(other) else other.times(this)
    }

    override fun div(other: NumberExpr): NumberExpr {
        //Any code calling this should check
        if (other.isZero)
            throw ArithmeticException("divide by zero")

        when (other) {
            is Integer -> {
                val gcd = this.gcd(other)

                if (gcd.isOne) {
                    if (other.isOne)
                        return this

                    return Rational(this, other)
                }

                //Divide both by GCD
                val a = Integer(value.divide(gcd.value))
                val b = Integer(other.value.divide(gcd.value))

                if (b.isOne)
                    return a

                return Rational(a, b)
            }
            is Rational -> return this * Rational(other.denominator, other.numerator)
            is Complex -> return Complex(this) / other
            else -> return toPrecision(other.precision) / other
        }
    }

    fun pow(other: Integer): NumberExpr {
        val intVal = other.asInteger().value.toInt()
        return if (intVal < 0)
            Rational(ONE, Integer(value.pow(-intVal))).eval()
        else
            Integer(value.pow(intVal))
    }

    infix fun pow(b: Rational): Expr {
        if (this.isOne)
            return this

        if (this.isNegative) {
            if (b.denominator.isOdd)
                return Minus(Times(Power(this.unaryMinus(), b))).eval()

            return Power(unaryMinus(), b) * I()
        }

        // Performance: Use faster method for square root
        /* One other method for any value but may need additional checks
             - Calculate using Math.pow() after converting to doubles
             - isWholeNumber = floor(value) && !java.lang.Double.isInfinite(value)
             - Then convert to integer and return
        */
        val a = this

        if (b == Rational.HALF) {
            // BigInteger.sqrtAndRemainder() needs Java 9 to work in Android
            val sqrt = BigIntegerMath.isqrt(a.value)
            if (sqrt * sqrt == a.value)
                return Integer(sqrt)
        }

        // factor out any numbers that are the Nth root of the denominator
        val t = Factor(a).eval()
        val factors = Tally(t).eval().asList()

        // All roots factored out, apply power / reduce to proper fraction
        if (factors.size == 1 && factors[0].asList()[1] == ONE) {
            if (b > ONE) {
                val whole = Integer(b.numerator.intValue() / b.denominator.intValue())

                return Power(a, whole) * Power(a, b - whole)
            }

            return Power(a, b)
        }

        // No factors means nothing to reduce
        if (factors.args.all { (it.asList()[1] as Integer).isOne })
            return Power(a, b)

        val items = mutableListOf<Expr>()
        for (factor in factors.args) {
            val n = factor.asList()[0] as Integer
            val x = factor.asList()[1] as Integer

            items.add(Power(n, b * x))
        }

        return Times(*items.toTypedArray()).eval()
    }

    override fun pow(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> this.pow(other)
            is RealDouble,
            is RealBigDec -> this.toPrecision(other.precision).pow(other)
            else -> throw UnsupportedOperationException()
        }
    }

    //IntegerNum Specific Functions
    fun gcd(N: Integer): Integer = Integer(value.gcd(N.value))

    fun powerMod(b: Integer, m: Integer): Integer {
        //Assuming all integers at this point since MathFunc needs to check that
        val num = value
        val exp = b.value
        val mod = m.value

        return Integer(num.modPow(exp, mod))
    }

    fun primeQ(): Boolean = value.isProbablePrime(5)

    operator fun inc(): Integer = Integer(value.inc())
    operator fun dec(): Integer = Integer(value.minus(BigInteger.ONE))
    operator fun rem(N: Integer): Integer = Integer(value.mod(N.value))

    override fun quotient(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> {
                var div = value.divide(other.value)
                if (div.signum() < 0) {
                    val test = div * other.value
                    if (test > value)
                        div -= BigInteger.ONE
                }

                return Integer(div)
            }
            is Complex -> (this / other).round()
            else -> (this / other).floor()
        }
    }

    override fun compareTo(other: NumberExpr): Int {
        return when (other) {
            is Integer -> value.compareTo(other.value)
            is Rational -> this.compareTo(other.toPrecision(MachinePrecision))
            is RealDouble,
            is RealBigDec -> toPrecision(other.precision).compareTo(other)
            is Complex -> Complex(this).compareTo(other)
            else -> throw NotImplementedError()
        }
    }

    override fun floor(): Integer = this
    override fun round(): NumberExpr = this
}

// TODO add more operators like this and replace use cases with them
operator fun Int.plus(other: NumberExpr): NumberExpr = Integer(this) + other
operator fun Int.minus(other: NumberExpr): NumberExpr = Integer(this) - other