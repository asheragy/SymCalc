package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Factor
import org.cerion.symcalc.expression.function.list.Tally
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class Integer(override val value: BigInteger) : NumberExpr() {
    companion object {
        @JvmField val ZERO = Integer(0)
        @JvmField val ONE = Integer(1)
        @JvmField val TWO = Integer(2)
        @JvmField val NEGATIVE_ONE = Integer(-1)
        val MAX_INT = Integer(Int.MAX_VALUE)
        val MIN_INT = Integer(Int.MIN_VALUE)
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

    override fun evaluate(precision: Int): NumberExpr {
        return when (precision) {
            InfinitePrecision -> this
            SYSTEM_DECIMAL_PRECISION -> RealDouble(value.toDouble())
            else -> {
                //if (isZero)
                //    return RealNum.create(0.0) // Prevents lost precision for BigDecimal(0)

                var bd = BigDecimal(value, MathContext(precision, RoundingMode.HALF_UP))
                bd = bd.setScale(precision)
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
                val n = other as Integer
                val gcd = this.gcd(n)

                if (gcd.isOne) {
                    return Rational(this, n)
                }

                //Divide both by GCD
                val a = Integer(value.divide(gcd.value))
                val b = Integer(n.value.divide(gcd.value))

                if (b.isOne)
                    return a

                return Rational(a, b)
            }
            NumberType.RATIONAL -> {
                other as Rational
                return Times(this, Rational(other.denominator, other.numerator)).eval() as NumberExpr
            }

            NumberType.REAL_DOUBLE,
            NumberType.REAL_BIGDEC -> return evaluate(other.precision) / other
            NumberType.COMPLEX -> return Complex(this) / other

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

    override fun compareTo(other: NumberExpr): Int {
        return when (other.numType) {
            NumberType.INTEGER -> value.compareTo(other.asInteger().value)
            NumberType.RATIONAL -> this.compareTo(other.evaluate(SYSTEM_DECIMAL_PRECISION))
            NumberType.REAL_DOUBLE,
            NumberType.REAL_BIGDEC -> evaluate(other.precision).compareTo(other)
            NumberType.COMPLEX -> Complex(this).compareTo(other)
        }
    }

    fun pow(other: Integer): NumberExpr {
        val intVal = other.asInteger().value.toInt()
        if (intVal < 0)
            return Rational(ONE, Integer(value.pow(-intVal))).evaluate()
        else
            return Integer(value.pow(intVal))
    }

    fun pow(b: Rational): Expr {
        // Performance: Use faster method for square root
        /* One other method for any value but may need additional checks
             - Calculate using Math.pow() after converting to doubles
             - isWholeNumber = floor(value) && !java.lang.Double.isInfinite(value)
             - Then convert to integer and return
        */
        val a = this

        if (b == Rational.HALF) {
            val sqrt = a.value.sqrtAndRemainder()
            if (sqrt[1].compareTo(BigInteger.ZERO) == 0)
                return Integer(sqrt[0])
        }

        // factor out any numbers that are the Nth root of the denominator
        val t = Factor(a).eval()
        val factors = Tally(t).eval().asList()

        var denominator = b.denominator
        val numerator = b.numerator
        var multiply = ONE

        var i = 0
        while (i < factors.size) {
            val key = factors[i][0].asInteger()
            val v = factors[i][1].asInteger()

            // TODO_LP seems like this function can be rewritten to be cleaner, recursion on each reduce step maybe

            // Factor it out
            if (v >= denominator) {
                multiply *= key
                val count = v - denominator
                factors[i] = ListExpr(key, count)
                if (count.isZero)
                    i++
            }
            else {
                if ((denominator % v).isZero && !v.isOne && denominator.isEven) {
                    factors[i] = ListExpr(key, v / TWO)
                    denominator = (denominator / TWO) as Integer
                }
                else {
                    i++
                }
            }
        }

        if (multiply.isOne && b.denominator == denominator)
            return Power(a, b)

        // Factor out multiples
        //Expr result = new Power(this, num);
        var root = ONE
        for (j in 0 until factors.size) {
            val f1 = factors[j][0]
            val f2 = factors[j][1] as Integer
            if (f2.isZero)
                continue

            root = Times(root, f1, f2).eval().asInteger()
        }

        val nthRoot = Times(multiply, Power(root, Rational(numerator, denominator)))
        return Power(nthRoot, numerator).eval()

    }
}
