package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr

/**
 * One frame of variable bindings.
 *
 * The root frame (no parent) holds global definitions made with Set. Child frames hold the
 * variables of one scoping construct (Table, Sum, Do, later Block/Module) and are discarded
 * when that construct returns. Depth equals the nesting of scopers, not the depth of the
 * expression tree; ordinary functions pass the same context straight through.
 */
class EvalContext private constructor(private val parent: EvalContext?) {

    private val vars = HashMap<String, Expr>()

    constructor() : this(null)

    /** Reads walk outward: this frame, then its parent, up to the root. */
    fun lookup(name: String): Expr? = vars[name] ?: parent?.lookup(name)

    /** New empty frame below this one. A scoper calls it once and evaluates its body in it. */
    fun child(): EvalContext = EvalContext(this)

    /**
     * Bind in this frame only. Scopers use this for their own variables, overwriting on each
     * iteration. Never walks to a parent.
     */
    operator fun set(name: String, value: Expr) {
        vars[name] = value
    }

    operator fun get(name: String): Expr? = lookup(name)

    /**
     * User assignment, x = 5. Writes to the innermost frame that already binds the name, so an
     * assignment to a Table/Block local updates the local; otherwise writes to the root.
     * This is the only write that can reach the root.
     */
    fun assign(name: String, value: Expr) {
        (frameBinding(name) ?: root()).vars[name] = value
    }

    /** Unset[x] / Clear[x]: remove the innermost binding of the name, if any. */
    fun unset(name: String) {
        frameBinding(name)?.vars?.remove(name)
    }

    private fun frameBinding(name: String): EvalContext? {
        var frame: EvalContext? = this
        while (frame != null) {
            if (name in frame.vars)
                return frame
            frame = frame.parent
        }
        return null
    }

    private fun root(): EvalContext {
        var frame = this
        while (true)
            frame = frame.parent ?: return frame
    }
}

/*

Only as deep as needed. Frames are created by scoping constructs, not by function calls. Everything else passes the same ctx reference straight through.

Who does what. There are really three cases, not two:

┌───────────────┬──────────────────────────────────────────────────────────────────┬─────────────────────────────────────────┐
│ Function kind │               What it does with ctx                              │                Examples                 │
├───────────────┼──────────────────────────────────────────────────────────────────┼─────────────────────────────────────────┤
│ Ordinary      │ Passes it through unchanged to sub-evaluations                   │ Plus, Sin, D, Map, N, almost everything │
├───────────────┼──────────────────────────────────────────────────────────────────┼─────────────────────────────────────────┤
│ User writer   │ Calls ctx.assign: innermost frame binding the name, else root    │ Set, SetDelayed, later Unset and Clear  │
├───────────────┼──────────────────────────────────────────────────────────────────┼─────────────────────────────────────────┤
│ Scoper        │ Calls ctx.child() once, ctx[name] = value per iteration, evaluates body in the child │ Table, Sum, Do, Block, Module, With │
└───────────────┴──────────────────────────────────────────────────────────────────┴─────────────────────────────────────────┘

Reads always walk outward. Writes never do, except assign, which walks only to find an existing binding and otherwise lands on the root.

Table[x = i, {i, 3}]; x       -> 3   (x is not localized, assign reaches the root)
Table[i = 5; i, {i, 3}]; i    -> i   (i is bound in the Table frame, assign updates the local; global i untouched)

Five-deep example. Plus[Sin[Times[Power[Set[x, 5], 2], 3]], 1]: the chain is one node, the root. Set assigns into it directly. No frames were created because none of those functions is a scoper.

Table[i, {i, 3}]: the chain is two nodes while the body runs: root, plus one frame holding i. The frame is created once and i is overwritten per iteration. When Table returns, the frame is garbage.

Table[Table[i + j, {j, 2}], {i, 2}]: three nodes deep during the inner body: root, i frame, j frame. A Table with two iterators {i, 2}, {j, 2} uses one frame holding both. Lookup cost is linear in the number of enclosing scopers.

*/
