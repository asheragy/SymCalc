package org.cerion.symcalc.expression.function.trig;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.number.NumberExpr;
import org.cerion.symcalc.expression.number.RealNum;

public class Cos extends TrigBase {

    public Cos(Expr...e) {
        super(FunctionType.COS, e);
    }

    @Override
    protected Expr evaluate(NumberExpr num) {
        if(!num.isComplex())
            return RealNum.Companion.create( Math.cos( num.toDouble() ));

        return this;
    }
}