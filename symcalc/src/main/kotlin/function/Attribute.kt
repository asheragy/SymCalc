package org.cerion.symcalc.function

enum class Attribute {
    Flat,              // Associative f(a, f(b, c)) -> f(a, b, c)
    Orderless,         // Order of parameters does not matter
    Listable,          // f({a, b, c}) -> {f(a), f(b), f(c)}
    NumericFunction,   // Function evaluates to number when all inputs are numbers
    HoldAll,           // Prevents args from being evaluated automatically
    HoldFirst,         // Do not evaluate first argument
    HoldRest,          // Only evaluate first argument
    //HoldAllComplete,
    OneIdentity,
    SequenceHold

}

/*
    // ssymb = Cases[Map[ToExpression, Names["System`*"]], _Symbol];
    // nfuns = Select[ssymb, MemberQ[Attributes[#], HoldFirst] &]

    Constant - Maybe unnecessary since not using constants as functions
    Locked NHoldAll NHoldFirst NHoldRest Stub Temporary Protect Protected ReadProtected Unprotect SetAttributes
 */
