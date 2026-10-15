package org.cerion.symcalc.function

enum class Attribute {
    Flat,              // Associative f(a, f(b, c)) -> f(a, b, c)
    Orderless,         // Order of parameters does not matter
    Listable,          // f({a, b, c}) -> {f(a), f(b), f(c)}
    HoldAll,           // Prevents args from being evaluated automatically
    NumericFunction    // Function evaluates to number when all inputs are numbers
    //HoldFirst,
    //HoldRest,
    //HoldAllComplete,
    //OneIdentity,
    //SequenceHold,

}

/*
    // ssymb = Cases[Map[ToExpression, Names["System`*"]], _Symbol];
    // nfuns = Select[ssymb, MemberQ[Attributes[#], HoldFirst] &]

    Constant - Maybe unnecessary since not using constants as functions
    Locked NHoldAll NHoldFirst NHoldRest Stub Temporary Protect Protected ReadProtected Unprotect SetAttributes
 */
