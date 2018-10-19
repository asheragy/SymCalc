package org.cerion.symcalc.expression.number

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

    override fun toString(): String = intVal.toString()
    override fun toDouble(): Double = intVal.toDouble()

    override fun equals(e: NumberExpr): Boolean {
        if (e.isInteger) {
            val n = e as IntegerNum
            if (intVal.compareTo(n.intVal) == 0)
                return true
        }

        return false
    }

    fun intValue(): Int = intVal.toInt()
    fun toBigInteger(): BigInteger = intVal
    fun toBigDecimal(): BigDecimal = BigDecimal(intVal)

    operator fun plus(N: IntegerNum): IntegerNum = IntegerNum(intVal.add(N.intVal))
    operator fun minus(n: IntegerNum): IntegerNum = IntegerNum(intVal.subtract(n.intVal))
    operator fun times(n: IntegerNum): IntegerNum = IntegerNum(intVal.multiply(n.intVal))

    override fun unaryMinus(): IntegerNum = IntegerNum(intVal.negate())

    override fun plus(number: NumberExpr): NumberExpr {
        return if (number.isInteger) plus(number.asInteger()) else number.plus(this)
    }

    override fun minus(number: NumberExpr): NumberExpr {
        if (number.isInteger)
            return minus(number.asInteger())

        //Default reverse order
        val negative = number.unaryMinus()
        return negative + this
    }

    override fun times(num: NumberExpr): NumberExpr {
        return if (num.isInteger) times(num.asInteger()) else num.times(this)
    }

    override fun div(num: NumberExpr): NumberExpr {
        //Any code calling this should check
        if (num.isZero)
            throw ArithmeticException("divide by zero")

        //Int / Int
        if (num.isInteger) {
            val n = num as IntegerNum
            val gcd = this.gcd(n)

            if (gcd.isOne) {
                return if (env.isNumericalEval) {
                    Divide(RealNum.create(toDouble()), RealNum.create(num.toDouble())).eval() as NumberExpr
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

    override fun canExp(num: NumberExpr): Boolean {
        return when (num.numType) {
            NumberType.INTEGER -> return true
            NumberType.REAL -> return true
            else -> false
        }
    }

    override fun power(num: NumberExpr): Expr {
        //NumberExpr result = null;
        when (num.numType) {
            NumberType.INTEGER -> return IntegerNum(intVal.pow(num.asInteger().intVal.toInt()))

            NumberType.RATIONAL -> {
                val pow = Math.pow(intVal.toDouble(), num.toDouble())
                val real = RealNum.create(pow)

                if (!env.isNumericalEval) {
                    if (real.isWholeNumber)
                        return real.toInteger()
                    else {
                        // factor out any numbers that are the Nth root of the denominator
                        val t = Factor(this)
                        val factors = Tally(t).eval().asList()
                        val denominator = num.asRational().denominator

                        var multiply = IntegerNum.ONE

                        run {
                            var i = 0
                            while (i < factors.size()) {
                                val key = factors[i][0].asInteger()
                                val v = factors[i][1].asInteger()

                                // Factor it out
                                if (v >= denominator) {
                                    multiply*= key
                                    factors[i] = ListExpr(key, v - denominator)
                                } else
                                    i++
                            }
                        }

                        if (multiply.isOne)
                            return Power(this, num)

                        // Factor out multiples
                        //Expr result = new Power(this, num);
                        var root = IntegerNum.ONE
                        for (i in 0 until factors.size()) {
                            root = Times(root, factors[i][0], factors[i][1]).eval().asInteger()
                        }

                        return Times(multiply, Power(root, num))
                    }
                }

                return real
            }

            NumberType.REAL -> {
            }//result = RealNum.create(this);
        }

        throw NotImplementedError()
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
    operator fun mod(N: IntegerNum): IntegerNum = IntegerNum(intVal.mod(N.intVal))

    override fun compareTo(other: NumberExpr): Int {
        if (other.isInteger) {
            val n1 = intVal
            val n2 = other.asInteger().intVal
            return n1.compareTo(n2)
        }

        throw NotImplementedError()
    }

    companion object {
        @JvmField val ZERO = IntegerNum(0)
        @JvmField val ONE = IntegerNum(1)
        @JvmField val TWO = IntegerNum(2)
        @JvmField val NEGATIVE_ONE = IntegerNum(-1)
    }
}