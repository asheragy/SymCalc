package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.NumberExpr
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
    val isEven: Boolean get() = !intVal.testBit(0)
    val isOdd: Boolean get() = intVal.testBit(0)

    constructor(n: BigInteger) {
        value = n
    }

    constructor(s: String) {
        value = BigInteger(s)
    }

    constructor(n: Long) {
        value = BigInteger.valueOf(n)
    }

    override fun numType(): Int = NumberExpr.INTEGER
    override fun toString(): String = intVal.toString()

    override fun equals(e: NumberExpr): Boolean {
        if (e.isInteger) {
            val n = e as IntegerNum
            if (intVal.compareTo(n.intVal) == 0)
                return true
        }

        return false
    }

    @Deprecated("")
    fun toInteger(): Int = intVal.toInt()

    fun intValue(): Int = intVal.toInt()
    fun toBigInteger(): BigInteger = intVal

    // TODO check where needed, this class can abstract some functionality
    fun toBigDecimal(): BigDecimal = BigDecimal(intVal)

    // TODO check where needed and possibly remove
    override fun toDouble(): Double = intVal.toDouble()

    override fun negate(): IntegerNum = IntegerNum(intVal.negate())

    // TODO check where needed, we have isEven/Odd
    fun signum(): Int = intVal.signum()

    fun add(n: IntegerNum): IntegerNum = IntegerNum(intVal.add(n.intVal))
    fun subtract(n: IntegerNum): IntegerNum = IntegerNum(intVal.subtract(n.intVal))
    fun multiply(n: IntegerNum): IntegerNum = IntegerNum(intVal.multiply(n.intVal))

    override fun add(num: NumberExpr): NumberExpr {
        return if (num.numType() == NumberExpr.INTEGER) add(num as IntegerNum) else num.add(this)
    }
    override fun subtract(num: NumberExpr): NumberExpr {
        if (num.numType() == NumberExpr.INTEGER)
            return subtract(num as IntegerNum)

        //Default reverse order
        val negative = num.negate()
        return negative.add(this)
    }

    override fun multiply(num: NumberExpr): NumberExpr {
        return if (num.numType() == NumberExpr.INTEGER) multiply(num as IntegerNum) else num.multiply(this)
    }

    override fun divide(num: NumberExpr): NumberExpr {
        //Any code calling this should check
        if (num.isZero)
            throw ArithmeticException("divide by zero")

        //Int / Int
        if (num.isInteger) {
            val n = num as IntegerNum
            val gcd = this.GCD(n)

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
        return when (num.numType()) {
            NumberExpr.INTEGER -> return true
            NumberExpr.REAL -> return true
            else -> false
        }
    }

    override fun power(num: NumberExpr): Expr {
        //NumberExpr result = null;
        when (num.numType()) {
            NumberExpr.INTEGER -> return IntegerNum(intVal.pow(num.asInteger().intVal.toInt()))

            NumberExpr.RATIONAL -> {
                val pow = Math.pow(intVal.toDouble(), num.toDouble())
                val real = RealNum.create(pow)

                if (!env.isNumericalEval) {
                    if (real.isWholeNumber)
                        return real.toInteger()
                    else {
                        // factor out any numbers that are the Nth root of the denominator
                        val t = Factor(this)
                        val factors = Tally(t).eval().asList()
                        val denominator = (num as RationalNum).denominator().intValue()

                        var multiply = IntegerNum.ONE

                        run {
                            var i = 0
                            while (i < factors.size()) {
                                val key = factors[i][0].asInteger().intValue()
                                val `val` = factors[i][1].asInteger().intValue()

                                // Factor it out
                                if (`val` >= denominator) {
                                    multiply = Times(multiply, IntegerNum(key.toLong())).eval().asInteger()
                                    factors[i] = ListExpr(IntegerNum(key.toLong()), IntegerNum((`val` - denominator).toLong()))
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

            NumberExpr.REAL -> {
            }//result = RealNum.create(this);
        }

        throw NotImplementedError()
    }

    //IntegerNum Specific Functions
    fun GCD(N: IntegerNum): IntegerNum = IntegerNum(intVal.gcd(N.intVal))

    // TODO change parameters to IntegerNum
    fun powerMod(b: NumberExpr, m: NumberExpr): IntegerNum {
        //Assuming all integers at this point since MathFunc needs to check that
        val num = intVal
        val exp = (b as IntegerNum).intVal
        val mod = (m as IntegerNum).intVal

        return IntegerNum(num.modPow(exp, mod))
    }

    fun primeQ(): Boolean = intVal.isProbablePrime(5)

    // TODO consider removing
    fun factorial(): IntegerNum = factorial(intVal.toLong().toInt())

    operator fun mod(N: IntegerNum): IntegerNum {
        return IntegerNum(intVal.mod(N.intVal))
    }

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

        // TODO move to class
        @JvmStatic
        fun factorial(N: Int): IntegerNum { var N = N
            var result = BigInteger.valueOf(1)
            while (N > 1) {
                result = result.multiply(BigInteger(N.toString() + ""))
                N--
            }

            return IntegerNum(result)
        }
    }
}
