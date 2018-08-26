package org.cerion.symcalc.expression.graphics;

import org.cerion.symcalc.UserFunction;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.GraphicsExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class PlotGraphics extends GraphicsExpr {

    private UserFunction function;
    private IntegerNum min;
    private IntegerNum max;

    public PlotGraphics(UserFunction function, IntegerNum min, IntegerNum max) {
        this.function = function;
        this.min = min;
        this.max = max;
    }

    @Override
    public void show(int i) {

        for(int x = min.intValue(); x <= max.intValue(); x++) {
            Expr e = function.eval(new IntegerNum(x));
            System.out.println(x + "\t" + e);
        }


    }
}
