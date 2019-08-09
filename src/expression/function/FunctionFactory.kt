package org.cerion.symcalc.expression.function

import expression.function.list.ConstantArray
import expression.function.logical.Equal
import expression.function.trig.ArcTan
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.calculus.D
import org.cerion.symcalc.expression.function.core.*
import org.cerion.symcalc.expression.function.integer.*
import org.cerion.symcalc.expression.function.list.*
import org.cerion.symcalc.expression.function.logical.*
import org.cerion.symcalc.expression.function.plots.*
import org.cerion.symcalc.expression.function.procedural.If
import org.cerion.symcalc.expression.function.statistics.*
import org.cerion.symcalc.expression.function.trig.*
import java.lang.RuntimeException

class FunctionFactory {

    companion object {

        fun createInstance(name: String, vararg e: Expr): FunctionExpr {
            when (stringToFunctionType(name)) {
                Function.N -> return N(*e)
                Function.HOLD -> return Hold(*e)
                Function.NUMBERQ -> return NumberQ(*e)
                Function.NUMERICQ -> return NumericQ(*e)
                Function.EQUAL -> return Equal(*e)

                Function.PLUS -> return Plus(*e)
                Function.SUBTRACT -> return Subtract(*e)
                Function.TIMES -> return Times(*e)
                Function.DIVIDE -> return Divide(*e)
                Function.POWER -> return Power(*e)
                Function.MINUS -> return Minus(*e)
                Function.LOG -> return Log(*e)
                Function.SQRT -> return Sqrt(*e)

                // Trig
                Function.SIN -> return Sin(*e)
                Function.COS -> return Cos(*e)
                Function.TAN -> return Tan(*e)
                Function.ARCTAN -> return ArcTan(*e)

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

                //IntegerNum
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

                // Calculus
                Function.D -> return D(*e)

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
                null -> throw RuntimeException("Function not found")
            }

            throw RuntimeException("Missing case")
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
}