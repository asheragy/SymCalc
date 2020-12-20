package org.cerion.symcalc.function

import org.cerion.symcalc.function.list.ConstantArray
import org.cerion.symcalc.function.logical.Equal
import org.cerion.symcalc.function.trig.ArcTan
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.function.calculus.*
import org.cerion.symcalc.function.core.*
import org.cerion.symcalc.function.integer.*
import org.cerion.symcalc.function.list.*
import org.cerion.symcalc.function.logical.Greater
import org.cerion.symcalc.function.numeric.Floor
import org.cerion.symcalc.function.plots.Plot
import org.cerion.symcalc.function.procedural.If
import org.cerion.symcalc.function.special.*
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
            Function.EXP -> return Exp(*e)
            Function.SQRT -> return Sqrt(*e)

            // Trig
            Function.SIN -> return Sin(e[0])
            Function.COS -> return Cos(e[0])
            Function.TAN -> return Tan(e[0])
            Function.ARCSIN -> return ArcSin(e[0])
            Function.ARCCOS -> return ArcCos(e[0])
            Function.ARCTAN -> return ArcTan(e[0])
            Function.COT -> return Cot(e[0])
            Function.SINH -> return Sinh(e[0])
            Function.COSH -> return Cosh(e[0])
            Function.TANH -> return Tanh(e[0])
            Function.ARCSINH -> return ArcSinh(e[0])
            Function.ARCCOSH -> return ArcCosh(e[0])
            Function.ARCTANH -> return ArcTanh(e[0])

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
            Function.VECTORQ -> return VectorQ(*e)
            Function.MATRIXQ -> return MatrixQ(*e)
            Function.DOT -> return Dot(*e)
            Function.CONSTANT_ARRAY -> return ConstantArray(*e)
            Function.TALLY -> return Tally(*e)
            Function.MAP -> return Map(*e)

            //Integer
            Function.FACTORIAL -> return Factorial(*e)
            Function.MOD -> return Mod(*e)
            Function.POWERMOD -> return PowerMod(*e)
            Function.GCD -> return GCD(*e)
            Function.FOURIER -> return Fourier(*e)
            Function.PRIMEQ -> return PrimeQ(*e)
            Function.FIBONACCI -> return Fibonacci(*e)
            Function.FACTOR -> return Factor(*e)
            Function.BINOMIAL -> return Binomial(*e)
            Function.BERNOULLI -> return Bernoulli(*e)
            Function.INTEGER_DIGITS -> return IntegerDigits(*e)
            Function.EVENQ -> return EvenQ(*e)
            Function.ODDQ -> return OddQ(*e)

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
            Function.IDENTITY_MATRIX -> return IdentityMatrix(*e)

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