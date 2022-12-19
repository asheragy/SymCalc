package org.cerion.symcalc.function

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.function.calculus.D
import org.cerion.symcalc.function.calculus.Sum
import org.cerion.symcalc.function.combinatorial.Binomial
import org.cerion.symcalc.function.combinatorial.Factorial
import org.cerion.symcalc.function.combinatorial.Factorial2
import org.cerion.symcalc.function.core.*
import org.cerion.symcalc.function.core.Set
import org.cerion.symcalc.function.hyperbolic.*
import org.cerion.symcalc.function.integer.*
import org.cerion.symcalc.function.list.*
import org.cerion.symcalc.function.list.Map
import org.cerion.symcalc.function.logical.Equal
import org.cerion.symcalc.function.logical.Greater
import org.cerion.symcalc.function.matrix.Dot
import org.cerion.symcalc.function.matrix.IdentityMatrix
import org.cerion.symcalc.function.matrix.MatrixQ
import org.cerion.symcalc.function.matrix.VectorQ
import org.cerion.symcalc.function.numeric.Floor
import org.cerion.symcalc.function.plots.Plot
import org.cerion.symcalc.function.procedural.If
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Pochhammer
import org.cerion.symcalc.function.special.PolyGamma
import org.cerion.symcalc.function.special.Zeta
import org.cerion.symcalc.function.statistics.*
import org.cerion.symcalc.function.trig.*

object FunctionFactory {

    fun createInstance(name: String, vararg e: Expr): FunctionExpr {
        val function = stringToFunctionType(name)
        if (function != null) {
            val result = createInstance(function, *e)

            require(result.size == e.size) { "expected ${result.size} parameters, actual ${e.size}" }

            return result
        }

        throw RuntimeException("Function not found")
    }

    // TODO can this be done with reflection
    private fun createInstance(f: Function, vararg e: Expr): FunctionExpr {
        when (f) {
            Function.N -> return N(*e)
            Function.HOLD -> return Hold(*e)
            Function.NUMBERQ -> return NumberQ(*e)
            Function.NUMERICQ -> return NumericQ(*e)
            Function.EQUAL -> return Equal(*e)

            Function.PLUS -> return Plus(*e)
            Function.SUBTRACT -> return Subtract(*e)
            Function.TIMES -> return Times(*e)
            Function.DIVIDE -> return Divide(e[0], e[1])
            Function.POWER -> return Power(*e)
            Function.MINUS -> return Minus(*e)
            Function.LOG -> return Log(*e)
            Function.LOG10 -> return Log10(*e)
            Function.EXP -> return Exp(*e)
            Function.SQRT -> return Sqrt(*e)

            // Trig
            Function.SIN -> return Sin(e[0])
            Function.COS -> return Cos(e[0])
            Function.TAN -> return Tan(e[0])
            Function.SEC -> return Sec(e[0])
            Function.CSC -> return Csc(e[0])
            Function.COT -> return Cot(e[0])
            Function.ARCSIN -> return ArcSin(e[0])
            Function.ARCCOS -> return ArcCos(e[0])
            Function.ARCTAN -> return ArcTan(*e)
            Function.ARCSEC -> return ArcSec(e[0])
            Function.ARCCSC -> return ArcCsc(e[0])
            Function.ARCCOT -> return ArcCot(e[0])

            // Hyperbolic
            Function.SINH -> return Sinh(e[0])
            Function.COSH -> return Cosh(e[0])
            Function.TANH -> return Tanh(e[0])
            Function.SECH -> return Sech(e[0])
            Function.CSCH -> return Csch(e[0])
            Function.COTH -> return Coth(e[0])
            Function.ARCSINH -> return ArcSinh(e[0])
            Function.ARCCOSH -> return ArcCosh(e[0])
            Function.ARCTANH -> return ArcTanh(e[0])
            Function.ARCSECH -> return ArcSech(e[0])
            Function.ARCCSCH -> return ArcCsch(e[0])
            Function.ARCCOTH -> return ArcCoth(e[0])

            //List
            Function.TOTAL -> return Total(*e)
            Function.RANGE -> return Range(*e)
            Function.REVERSE -> return Reverse(*e)
            Function.FIRST -> return First(*e)
            Function.LAST -> return Last(*e)
            Function.TABLE -> return Table(*e)
            Function.FLATTEN -> return Flatten(*e)
            Function.PARTITION -> return Partition(*e)
            Function.JOIN -> return Join(*e)
            Function.SELECT -> return Select(*e)
            Function.CONSTANT_ARRAY -> return ConstantArray(*e)
            Function.TALLY -> return Tally(*e)
            Function.MAP -> return Map(*e)

            // Matrix
            Function.VECTORQ -> return VectorQ(*e)
            Function.MATRIXQ -> return MatrixQ(*e)
            Function.IDENTITY_MATRIX -> return IdentityMatrix(*e)
            Function.DOT -> return Dot(*e)

            //Integer
            Function.MOD -> return Mod(*e)
            Function.POWERMOD -> return PowerMod(*e)
            Function.GCD -> return GCD(*e)
            Function.FOURIER -> return Fourier(*e)
            Function.PRIMEQ -> return PrimeQ(*e)
            Function.FIBONACCI -> return Fibonacci(*e)
            Function.FACTOR -> return Factor(*e)
            Function.BERNOULLI -> return Bernoulli(*e)
            Function.INTEGER_DIGITS -> return IntegerDigits(*e)
            Function.EVENQ -> return EvenQ(*e)
            Function.ODDQ -> return OddQ(*e)
            Function.EULERPHI -> return EulerPhi(*e)
            Function.EULERE -> return EulerE(*e)
            Function.DIVISORS -> return Divisors(*e)
            Function.DIVISORSIGMA -> return DivisorSigma(*e)

            // Combinatorial
            Function.FACTORIAL -> return Factorial(*e)
            Function.FACTORIAL2 -> return Factorial2(*e)
            Function.BINOMIAL -> return Binomial(*e)

            // Numeric
            Function.FLOOR -> return Floor(*e)

            // Calculus
            Function.D -> return D(*e)
            Function.SUM -> return Sum(*e)

            //Logical
            Function.GREATER -> return Greater(*e)

            //Statistics
            Function.MEAN -> return Mean(*e)
            Function.VARIANCE -> return Variance(*e)
            Function.STANDARD_DEVIATION -> return StandardDeviation(*e)
            Function.RANDOM_CHOICE -> return RandomChoice(*e)
            Function.RANDOM_INTEGER -> return RandomInteger(*e)

            // Graphics
            Function.PLOT -> return Plot(*e)

            // Categorize these
            Function.SET -> return Set(*e)
            Function.COMPOUND_EXPRESSION -> return CompoundExpression(*e)

            Function.IF -> return If(*e)

            // Special
            Function.GAMMA -> return Gamma(*e)
            Function.POCHHAMMER -> return Pochhammer(*e)
            Function.POLYGAMMA -> return PolyGamma(*e)
            Function.ZETA -> return Zeta(*e)

            Function.END_OF_LIST -> throw IllegalArgumentException()
        }
    }

    @JvmStatic
    fun isValidFunction(functionName: String): Boolean {
        val name = functionName.toLowerCase()

        return stringToFunctionType(name) != null
    }

    private fun stringToFunctionType(functionName: String): Function? {
        val name = functionName.toLowerCase()
        for (t in Function.values()) {
            if (t.value.toLowerCase().contentEquals(name))
                return t
        }

        return null
    }

}