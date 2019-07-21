package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import java.math.RoundingMode

class RationalNum @JvmOverloads constructor(n: IntegerNum, d: IntegerNum = IntegerNum.ONE) : NumberExpr() {

    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL
    override val isNegative: Boolean get() = numerator.isNegative

    val numerator: IntegerNum get() = get(0) as IntegerNum
    val denominator: IntegerNum get() = get(1) as IntegerNum

    init {
        if (d.signum == -1)
            set(n.unaryMinus(), d.unaryMinus())
        else
            set(n, d)
    }

    constructor(n: Int, d: Int) : this(IntegerNum(n.toLong()), IntegerNum(d.toLong()))

    override fun evaluate(): NumberExpr {
        //Reduce with GCD
        val gcd = numerator.gcd(denominator)
        var n: IntegerNum
        var d: IntegerNum

        if (!gcd.isOne) {
            n = numerator.div(gcd).asInteger()
            d = denominator.div(gcd).asInteger()
            set(n, d)
        }

        //Never allow bottom to be negative
        if (denominator.signum == -1) {
            n = numerator.unaryMinus()
            d = denominator.unaryMinus()
            set(n, d)
        }

        if(env.isNumericalEval) {
            if (env.precision > 0) {
                val a = numerator.toBigDecimal()
                val b = denominator.toBigDecimal()
                val t = a.divide(b, env.precision, RoundingMode.HALF_UP)
                return RealNum.create(t)
            }

            return RealNum.create(numerator.toDouble() / denominator.toDouble())
        }

        //Integer since denominator is one
        return if (denominator.isOne) numerator else this
    }

    override fun toString(): String = numerator.toString() + "/" + denominator.toString()

    override fun toDouble(): Double {
        val decN = numerator.toBigDecimal().toDouble()
        val decD = denominator.toBigDecimal().toDouble()
        return decN / decD
    }

    override fun unaryMinus(): NumberExpr = RationalNum(numerator.unaryMinus(), denominator)

    fun reciprocal(): NumberExpr = RationalNum(denominator, numerator).evaluate()

    override fun plus(other: NumberExpr): NumberExpr {
        val result: RationalNum
        when (other.numType) {
            NumberType.INTEGER -> {
                val norm = other.asInteger() * denominator
                return RationalNum(numerator.plus(norm), denominator)
            }

            NumberType.RATIONAL -> {
                val div = other as RationalNum
                val n1 = div.numerator * denominator
                val n2 = numerator * div.denominator

                result = RationalNum(n1 + n2, denominator * div.denominator)
                return result.evaluate()
            }
            else -> return other + this
        }
    }

    override fun minus(other: NumberExpr): NumberExpr = this + other.unaryMinus()

    override fun times(other: NumberExpr): NumberExpr {
        val result: RationalNum

        when (other.numType) {
            NumberType.INTEGER -> {
                result = RationalNum(numerator * other.asInteger(), denominator)
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = other as RationalNum
                result = RationalNum(numerator * t.numerator, denominator * t.denominator)
                return result.evaluate()
            }
            else -> return other * this
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        val result: RationalNum
        when (other.numType) {
            NumberType.INTEGER -> {
                result = RationalNum(numerator, denominator * other.asInteger())
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = other as RationalNum
                result = RationalNum(numerator * t.denominator, denominator * t.numerator)
                return result.evaluate()
            }
            else -> return other * this
        }
    }

    override fun canExp(other: NumberExpr): Boolean = !(other.isRational || other.isComplex)

    override fun power(other: NumberExpr): Expr {
        val n: IntegerNum
        val d: IntegerNum
        when (other.numType) {
            NumberType.INTEGER -> {
                n = numerator.power(other) as IntegerNum
                d = denominator.power(other) as IntegerNum
                return RationalNum(n, d)
            }

            //case RATIONAL: //RationalNum ^ RationalNum
            //	break;

            NumberType.REAL -> {
                val rResult = RealNum.create(this.toDouble())
                return rResult.power(other)
            }
            NumberType.RATIONAL -> {
                // If both sides are reduced there may be nothing to do here...
                return this
            }

            NumberType.COMPLEX -> {
                val complex = other.asComplex()
                if (complex.img.isZero)
                    return this.power(complex.real)

                TODO()
            }
        }
    }

    private operator fun set(n: IntegerNum?, d: IntegerNum?) {
        if (n != null)
            setArg(0, n)
        if (d != null)
            setArg(1, d)
    }

    override fun compareTo(other: NumberExpr): Int {
        // TODO add test to see if 1/2 == 2/4 and check compareTo Complex
        val result = toDouble().compareTo(other.toDouble())
        return result
    }

    companion object {
        val ZERO = RationalNum(IntegerNum.ZERO, IntegerNum.ONE)
        val ONE = RationalNum(IntegerNum.ONE, IntegerNum.ONE)
    }
}