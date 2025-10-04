# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SymCalc is a Kotlin symbolic math library modeled after Mathematica. It provides symbolic computation capabilities including algebraic manipulation, calculus, and numerical evaluation.

## Build System

Multi-module Gradle project with Kotlin 1.7.10:
- **symcalc**: Core symbolic math library
- **bignum**: Arbitrary precision number library
- **benchmark**: Performance benchmarks
- **desktopUI**: Jetpack Compose desktop UI (multiplatform)

## Common Commands

### Building
```bash
./gradlew build
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :symcalc:test
./gradlew :bignum:test

# Run single test class
./gradlew :symcalc:test --tests "org.cerion.symcalc.function.calculus.DTest"
```

### Cleaning
```bash
./gradlew clean
```

## Architecture

### Core Expression System

All expressions inherit from `Expr` (symcalc/src/main/kotlin/expression/Expr.kt):
- **AtomExpr**: Interface for atomic expressions with a single value
- **MultiExpr**: Base class for expressions with multiple arguments (arrays)
- Expression types: NUMBER, VARIABLE, FUNCTION, LIST, CONST, ERROR, BOOL, SYMBOL

Key expression classes:
- **ListExpr**: Represents lists/arrays `{1, 2, 3}`
- **VarExpr**: Variables like `x`, `y`
- **FunctionExpr**: Base class for all mathematical functions
- **NumberExpr**: Base class for numbers (Integer, Rational, RealDouble, RealBigDec, Complex)
- **BoolExpr**, **ConstExpr**, **ErrorExpr**, **SymbolExpr**: Other expression types

### Function System

Functions are organized by category in `symcalc/src/main/kotlin/function/`:
- **arithmetic**: Plus, Times, Power, Log, Sqrt, etc.
- **calculus**: D (derivative), Sum
- **trig**: Sin, Cos, Tan, Arc* variants
- **hyperbolic**: Sinh, Cosh, Tanh, Arc* variants
- **list**: Range, Table, Map, Select, Join, etc.
- **integer**: Factorial, Mod, GCD, Fibonacci, Factor, etc.
- **logical**: Equal, Greater
- **statistics**: Mean, Variance, StandardDeviation
- **special**: Gamma, Zeta, PolyGamma
- **matrix**: Dot, IdentityMatrix, VectorQ, MatrixQ
- **core**: N (numerical evaluation), Set, Hold

All functions extend `FunctionExpr` and implement `evaluate()`. Functions follow the Mathematica evaluation model.

### Number System

The `bignum` module provides arbitrary precision arithmetic:
- Independent module used by symcalc
- Supports very large integers and high-precision decimals
- Has separate test suite including long-running tests

### Evaluation

Functions follow Mathematica's evaluation procedure:
1. Arguments are evaluated first
2. Function-specific `evaluate()` is called
3. Results can be further simplified

Use `eval()` to evaluate expressions. Use `eval(precision: Int)` for numerical evaluation with specified precision.

### Tests

Test files use:
- JUnit 5 for most tests
- TestNG in some modules
- Test sources: `symcalc/src/test/java/` (note: Java directory but contains Kotlin files)
- Helper functions in `Helpers.kt` including custom assertion DSL

## Key Patterns

### Creating Expressions
```kotlin
// From constructors
ListExpr(1, 2, 3)
Integer(42)
VarExpr("x")

// From strings (parsing)
Expr.parse("x^2 + 2*x + 1")

// Using operators (extension functions)
val x = VarExpr("x")
x + 1  // Plus(x, Integer(1))
x * 2  // Times(x, Integer(2))
```

### Accessing Function Arguments
```kotlin
// FunctionExpr and ListExpr extend MultiExpr
val size = expr.size
val arg = expr[0]
val listArg = expr.getList(1)
val intArg = expr.getInteger(2)
```

### Environment Variables
Functions can access/set variables via `env`:
```kotlin
getEnvVar(name: String): Expr?
setEnvVar(name: String, e: Expr)
```

## Module Structure

- `symcalc/src/main/kotlin/`: Core library source
  - `expression/`: Expression type definitions
  - `function/`: Function implementations
  - `number/`: Number types
  - `constant/`: Mathematical constants (Pi, E, etc.)
  - `parser/`: Expression parser (Lexer, Parser)
  - `exception/`: Custom exceptions

- Tests mirror the main structure under `symcalc/src/test/java/`
