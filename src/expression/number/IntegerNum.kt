package org.cerion.symcalc.expression.number

import jdk.jshell.spi.ExecutionControl
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Factor
import org.cerion.symcalc.expression.function.list.Tally

import java.math.BigDecimal
import java.math.BigInteger

class IntegerNum : NumberExpr {

    private val intVal: BigInteger get() = value as BigInteger

    override val isZero: Boolean get() = intVal == BigInteger.ZERO
    override val isOne: Boolean get() = intVal == BigInteger.ONE
    override val numType: NumberType get() = NumberType.INTEGER
    override val isNegative: Boolean get() = signum == -1

    val isEven: Boolean get() = !intVal.testBit(0)
    val isOdd: Boolean get() = intVal.testBit(0)
    val signum: Int get() = intVal.signum()

    constructor(n: BigInteger) {
        value = n
    }

    constructor(s: String) {
        value = BigInteger(s)
    }

    constructor(n: Long) {
        value = BigInteger.valueOf(n)
    }

    constructor(n: Int) {
        value = BigInteger.valueOf(n.toLong())
    }

    override fun toString(): String = intVal.toString()
    override fun toDouble(): Double = intVal.toDouble()

    /*
    override fun equals(e: NumberExpr): Boolean {
        if (e.isInteger) {
            val n = e as IntegerNum
            if (intVal.compareTo(n.intVal) == 0)
                return true
        }

        return false
    }
    */

    fun intValue(): Int = intVal.toInt()
    fun toBigInteger(): BigInteger = intVal
    fun toBigDecimal(): BigDecimal = BigDecimal(intVal)

    operator fun plus(N: IntegerNum): IntegerNum = IntegerNum(intVal.add(N.intVal))
    operator fun minus(n: IntegerNum): IntegerNum = IntegerNum(intVal.subtract(n.intVal))
    operator fun times(n: IntegerNum): IntegerNum = IntegerNum(intVal.multiply(n.intVal))

    override fun unaryMinus(): IntegerNum = IntegerNum(intVal.negate())

    override fun plus(other: NumberExpr): NumberExpr {
        return if (other.isInteger) plus(other.asInteger()) else other.plus(this)
    }

    override fun minus(other: NumberExpr): NumberExpr {
        if (other.isInteger)
            return minus(other.asInteger())

        //Default reverse order
        val negative = other.unaryMinus()
        return negative + this
    }

    override fun times(other: NumberExpr): NumberExpr {
        return if (other.isInteger) times(other.asInteger()) else other.times(this)
    }

    override fun div(other: NumberExpr): NumberExpr {
        //Any code calling this should check
        if (other.isZero)
            throw ArithmeticException("divide by zero")

        //Int / Int
        if (other.isInteger) {
            val n = other as IntegerNum
            val gcd = this.gcd(n)

            if (gcd.isOne) {
                return if (env.isNumericalEval) {
                    Divide(RealNum.create(toDouble()), RealNum.create(other.toDouble())).eval() as NumberExpr
                } else RationalNum(this, n)

            }

            //Divide both by GCD
            val a = IntegerNum(intVal.divide(gcd.intVal))
            val b = IntegerNum(n.intVal.divide(gcd.intVal))

            if (b.isOne)
                return a

            return if (env.isNumericalEval) {
                Divide(RealNum.create(a.toDouble()), RealNum.create(b.toDouble())).eval() as NumberExpr
            } else RationalNum(a, b)

        }

        throw NotImplementedError()
    }

    override fun canExp(other: NumberExpr): Boolean {
        return when (other.numType) {
            NumberType.INTEGER -> return true
            NumberType.REAL -> return true
            else -> false
        }
    }

    override fun power(other: NumberExpr): NumberExpr {
        //NumberExpr result = null;
        when (other.numType) {
            NumberType.INTEGER -> return IntegerNum(intVal.pow(other.asInteger().intVal.toInt()))

            NumberType.RATIONAL -> {
                throw UnsupportedOperationException()
            }

            NumberType.REAL -> {
                val real = RealNum.create(this)
                return real.power(other)
            }
            NumberType.COMPLEX -> {
                val complex = other.asComplex()
                if (complex.img.isZero)
                    return this.power(complex.real)

                return ComplexNum(this).power(other)
            }
        }
    }

    //IntegerNum Specific Functions
    fun gcd(N: IntegerNum): IntegerNum = IntegerNum(intVal.gcd(N.intVal))

    fun powerMod(b: IntegerNum, m: IntegerNum): IntegerNum {
        //Assuming all integers at this point since MathFunc needs to check that
        val num = intVal
        val exp = b.intVal
        val mod = m.intVal

        return IntegerNum(num.modPow(exp, mod))
    }

    fun primeQ(): Boolean = intVal.isProbablePrime(5)

    operator fun inc(): IntegerNum = IntegerNum(intVal.inc())
    operator fun dec(): IntegerNum = IntegerNum(intVal.minus(BigInteger.ONE))
    operator fun rem(N: IntegerNum): IntegerNum = IntegerNum(intVal.mod(N.intVal))

    override fun compareTo(other: NumberExpr): Int {
        when (other.numType) {
            NumberType.INTEGER -> {
                val n1 = intVal
                val n2 = other.asInteger().intVal
                return n1.compareTo(n2)
            }

            NumberType.RATIONAL -> TODO()
            NumberType.REAL -> {
                val n1 = RealNum.create(this)
                return n1.compareTo(other)
            }
            NumberType.COMPLEX -> {
                val complex = other.asComplex()
                if (complex.img.isZero)
                    return this.compareTo(complex.real)

                TODO()
            }
        }
    }

    companion object {
        @JvmField val ZERO = IntegerNum(0)
        @JvmField val ONE = IntegerNum(1)
        @JvmField val TWO = IntegerNum(2)
        @JvmField val NEGATIVE_ONE = IntegerNum(-1)
    }
}
