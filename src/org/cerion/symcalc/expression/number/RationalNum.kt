package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr

class RationalNum @JvmOverloads constructor(n: IntegerNum, d: IntegerNum = IntegerNum.ONE) : NumberExpr() {

    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL

    val numerator: IntegerNum get() = get(0) as IntegerNum
    val denominator: IntegerNum get() = get(1) as IntegerNum

    init {
        if (d.signum == -1)
            set(n.unaryMinus(), d.unaryMinus())
        else
            set(n, d)
    }

    constructor(n: Int, d: Int) : this(IntegerNum(n.toLong()), IntegerNum(d.toLong()))

    // TODO add compareTo and equals can build off that
    override fun equals(e: NumberExpr): Boolean {
        if (e.isRational) {
            val r = e as RationalNum
            return numerator.equals(r.numerator) && denominator.equals(r.denominator)
        }

        return false
    }

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

        //Integer since denominator is one
        return if (denominator.isOne) numerator else this
    }

    override fun toString(): String = numerator.toString() + "/" + denominator.toString()

    override fun toDouble(): Double {
        var decN = numerator.toBigDecimal()
        val decD = denominator.toBigDecimal()
        decN = decN.divide(decD)
        return decN.toDouble()
    }

    override fun unaryMinus(): NumberExpr = RationalNum(numerator.unaryMinus(), denominator)

    fun reciprocal(): NumberExpr = RationalNum(denominator, numerator).evaluate()

    override fun plus(number: NumberExpr): NumberExpr {
        val result: RationalNum
        when (number.numType) {
            NumberType.INTEGER -> {
                val norm = number.asInteger() * denominator
                return RationalNum(numerator.plus(norm), denominator)
            }

            NumberType.RATIONAL -> {
                val div = number as RationalNum
                val n1 = div.numerator * denominator
                val n2 = numerator * div.denominator

                result = RationalNum(n1 + n2, denominator * div.denominator)
                return result.evaluate()
            }
        }

        return number + this //Default reverse order
    }

    override fun minus(number: NumberExpr): NumberExpr = this + number.unaryMinus()

    override fun times(num: NumberExpr): NumberExpr {
        val result: RationalNum
        when (num.numType) {
            NumberType.INTEGER -> {
                result = RationalNum(numerator * num.asInteger(), denominator)
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = num as RationalNum
                result = RationalNum(numerator * t.numerator, denominator * t.denominator)
                return result.evaluate()
            }
        }

        return num * this
    }

    override fun div(num: NumberExpr): NumberExpr {
        val result: RationalNum
        when (num.numType) {
            NumberType.INTEGER -> {
                result = RationalNum(numerator, denominator * num.asInteger())
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = num as RationalNum
                result = RationalNum(numerator * t.denominator, denominator * t.numerator)
                return result.evaluate()
            }
        }

        return num.times(this)
    }

    override fun canExp(num: NumberExpr): Boolean = !(num.isRational || num.isComplex)

    override fun power(num: NumberExpr): Expr {
        val n: IntegerNum
        val d: IntegerNum
        when (num.numType) {
            NumberType.INTEGER -> {
                n = numerator.power(num) as IntegerNum
                d = denominator.power(num) as IntegerNum
                return RationalNum(n, d)
            }

            //case RATIONAL: //RationalNum ^ RationalNum
            //	break;

            NumberType.REAL -> {
                val rResult = RealNum.create(this)
                return rResult.power(num)
            }
        }

        return ErrorExpr("Not implemented")
    }

    private operator fun set(n: IntegerNum?, d: IntegerNum?) {
        if (n != null)
            setArg(0, n)
        if (d != null)
            setArg(1, d)
    }

    override fun compareTo(o: NumberExpr): Int {
        throw NotImplementedError()
    }

    companion object {
        val ZERO = RationalNum(IntegerNum.ZERO, IntegerNum.ONE)
        val ONE = RationalNum(IntegerNum.ONE, IntegerNum.ONE)
    }
}