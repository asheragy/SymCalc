package org.cerion.symcalc.expression.function.calculus;


import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Subtract;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.function.trig.Cos;
import org.cerion.symcalc.expression.function.trig.Sin;
import org.cerion.symcalc.expression.number.IntegerNum;

public class D extends FunctionExpr {

    public D(Expr... e) {
        super(FunctionType.D,e);
    }

    @Override
    protected Expr evaluate() {
        Expr e = get(0);
        VarExpr var = (VarExpr)get(1);

        if (e.isNumber() || e.isConst())
            return IntegerNum.ZERO;

        if (e.isVariable()) {
            if (e.asVar().equals(var))
                return IntegerNum.ONE;
            else
                return IntegerNum.ZERO;
        }

        if (e.isFunction()) {
            FunctionExpr func = (FunctionExpr)e;
            FunctionExpr result = this;

            switch (func.getFunctionType()) {
                case PLUS:
                    // TODO use map function here, or similar
                    result = new Plus();
                    for(Expr ee : func.args())
                        result.add(new D(ee, var));
                    break;

                case SUBTRACT:
                    result = new Subtract();
                    for(Expr ee : func.args())
                        result.add(new D(ee, var));
                    break;

                case SIN:
                    result = new Times(
                            new D(func.get(0), var),
                            new Cos(func.get(0)));
                    break;

                case COS:
                    result = new Times(
                            new IntegerNum(-1),
                            new D(func.get(0), var),
                            new Sin(func.get(0)));
                    break;

                default:
                    return this;
            }

            return result.eval();
        }

        return this;
    }
}
