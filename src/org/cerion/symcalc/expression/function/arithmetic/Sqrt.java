package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.RationalNum;

public class Sqrt extends FunctionExpr {
    public Sqrt(Expr... e) {
        super(FunctionType.SQRT,e);
    }

    @Override
    protected Expr evaluate() {
        return new Power(get(0), new RationalNum(1,2)).eval();
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
    }
}
