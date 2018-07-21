package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.exception.ValidationException;
import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.arithmetic.Times;
import org.cerion.symcalc.expression.number.IntegerNum;

import java.rmi.server.ExportException;

public class Dot extends FunctionExpr {

    public Dot(Expr... e) {
        super(FunctionType.DOT,e);
    }

    @Override
    protected Expr evaluate() {

        ListExpr a = getList(0);
        ListExpr b = getList(1);
        if (new VectorQ(a).eval().toBool() == BoolExpr.TRUE)
            return evalVector(a, b);

        return evalMatrix(a, b);
    }

    private Expr evalVector(ListExpr a, ListExpr b) {
        Plus sum = new Plus();

        for(int i = 0; i < a.size(); i++) {
            sum.add(new Times(a.get(i), b.get(i)));
        }

        return sum.eval();
    }

    private Expr evalMatrix(ListExpr a, ListExpr b) {
        ListExpr result = new ListExpr();

        for(int i = 0; i < a.size(); i++) {
            ListExpr ax = a.getList(i);
            ListExpr sublist = new ListExpr();

            for(int j = 0; j < a.size(); j++) {
                Plus sum = new Plus();

                for(int k = 0; k < ax.size(); k++)
                    sum.add(new Times(
                            ax.get(k),
                            b.get(k).get(j)
                    ));

                sublist.add(sum.eval());
            }

            result.add(sublist);
        }

        return result;
    }

    @Override
    public void validate() throws ValidationException {
        validateParameterCount(2);
        validateParameterType(0, ExprType.LIST);
        validateParameterType(1, ExprType.LIST);

        ListExpr a = getList(0);
        ListExpr b = getList(1);

        if (new VectorQ(a).eval().toBool().value() && new VectorQ(b).eval().toBool().value()) {
            if (a.size() != b.size())
                throw new ValidationException("Vectors must be same length");
        }
        else if (new MatrixQ(a).eval().toBool().value() && new MatrixQ(b).eval().toBool().value()) {
            ListExpr ax = a.getList(0);
            ListExpr bx = b.getList(0);

            if (a.size() != bx.size() || b.size() != ax.size())
                throw new ValidationException("Incompatible matrix sizes");
        }
        else {
            throw new ValidationException("Arrays must be the same rank");
        }
    }
}
