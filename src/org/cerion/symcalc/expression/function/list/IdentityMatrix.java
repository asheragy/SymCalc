package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;

public class IdentityMatrix extends FunctionExpr {

    public IdentityMatrix(Expr... e) {
        super(FunctionType.IDENTITY_MATRIX,e);
    }

    @Override
    protected Expr evaluate() {
        int n = getInteger(0).intValue();
        ListExpr result = new ListExpr();

        for(int i = 0; i < n; i++) {
            ListExpr sublist = new ListExpr();
            for(int j = 0; j < n; j++)
                sublist.add(i == j ? IntegerNum.ONE : IntegerNum.ZERO);

            result.add(sublist);
        }

        return result;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(1);
        validateParameterType(0, ExprType.NUMBER);
        validateNumberType(0, NumberExpr.INTEGER);

        int n = getInteger(0).intValue();
        if (n <= 0)
            throw new ValidationException("Number must be a positive integer");
    }
}
