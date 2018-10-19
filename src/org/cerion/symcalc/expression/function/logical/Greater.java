package org.cerion.symcalc.expression.function.logical;

import org.cerion.symcalc.expression.*;
import org.cerion.symcalc.expression.number.NumberExpr;

import java.util.List;

public class Greater extends FunctionExpr {

    public Greater(Expr... e) {
        super(FunctionType.GREATER, e);
    }

    @Override
    protected Expr evaluate() {
        /*
            More than 2 elements are expanded to a > b > c > ...
            Simplified when possible
                 x > 2 > 1         = x > 2
                 x > 5 > 3 > 1 > y = x > 5 > 1 > y
         */

        // Simplify
        List<Expr> args = getAll();
        for(int i = 0; i < args.size() - 1; i++) {
            LogicalCompare comp = compare(args.get(i), args.get(i+1));
            if(comp == LogicalCompare.TRUE) {
                if(i == 0) { //First
                    args.remove(0);
                    i--;
                } else if(i == size() - 2) { //Last
                    args.remove(i+1);
                } else {
                    //If the next is also true, we can remove current
                    if(compare(args.get(i+1), args.get(i+2)) == LogicalCompare.TRUE) {
                        args.remove(i+1);
                        i--;
                    }
                }
            }
        }

        // If elements were removed evaluate new instance
        if(args.size() < size())
            return new Greater(args.toArray(new Expr[args.size()])).eval();

        if(size() == 1)
            return BoolExpr.TRUE;
        else {

            for(int i = 0; i < size() - 1; i++) {
                LogicalCompare comp = compare(get(i), get(i+1));
                if(comp == LogicalCompare.ERROR)
                    return new ErrorExpr("invalid comparison");
                if(comp == LogicalCompare.FALSE)
                    return BoolExpr.FALSE;
                if(comp == LogicalCompare.UNKNOWN)
                    return this;
            }

            return BoolExpr.TRUE;
        }
    }


    private LogicalCompare compare(Expr e1, Expr e2) {

        if(e1.isNumber() && e2.isNumber()) {
            NumberExpr n1 = (NumberExpr)e1;
            NumberExpr n2 = (NumberExpr)e2;
            if(n1.isComplex() || n2.isComplex())
                return LogicalCompare.ERROR;

            if(n1.toDouble() > n2.toDouble())
                return LogicalCompare.TRUE;
            else
                return LogicalCompare.FALSE;
        }

        return LogicalCompare.UNKNOWN;
    }
}
