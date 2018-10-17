package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.constant.Pi;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.function.statistics.RandomInteger;
import org.cerion.symcalc.expression.function.trig.Sin;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableTest {

    @Test
    public void validate() {
        // 0 parameters
        assertEquals(Expr.ExprType.ERROR, new Table().eval().getType());

        // 1 parameter
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2)).eval().getType());

        // 3 parameters
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new IntegerNum(1)), new IntegerNum(3)).eval().getType());

        // 2nd parameter is a list
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new IntegerNum(3)).eval().getType());
    }

    @Test
    public void validate_list() {
        // List must not be empty
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr()).eval().getType());

        // If copies, first list parameter must be integer
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new RationalNum(2,3))).eval().getType());

        // If 2+ parameters first must be a variable
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new IntegerNum(2), new IntegerNum(3))).eval().getType());

        // if 2,3,4 parameters others must be integer
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new VarExpr("a"), new RationalNum(1,2))).eval().getType());
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new VarExpr("a"), new IntegerNum(5), new Pi())).eval().getType());
        assertEquals(Expr.ExprType.ERROR, new Table(new IntegerNum(2), new ListExpr(new VarExpr("a"), new IntegerNum(5), new IntegerNum(6), RealNum.create(2.34))).eval().getType());
    }

    @Test
    public void copies() {
        // Table(3, 4) = {3,3,3,3}
        assertEquals(new ListExpr(new IntegerNum(3), new IntegerNum(3), new IntegerNum(3), new IntegerNum(3)),
                new Table(new IntegerNum(3),
                        new ListExpr(new IntegerNum(4)))
                        .eval());

        // Table(Sin(x), 3) = {Sin(x), Sin(x), Sin(x)}
        assertEquals(new ListExpr(new Sin(new VarExpr("x")), new Sin(new VarExpr("x")), new Sin(new VarExpr("x"))),
                new Table(new Sin(new VarExpr("x")),
                        new ListExpr(new IntegerNum(3)))
                        .eval());

        // Table(2 + 5, 2) = {7,7}
        assertEquals(new ListExpr(new IntegerNum(7), new IntegerNum(7)),
                new Table(new Plus(new IntegerNum(2), new IntegerNum(5)),
                        new ListExpr(new IntegerNum(2)))
                        .eval());
    }

    @Test
    public void range_oneToMax() {
        // Table(3, a, 5) = {3,3,3,3,3}
        assertEquals(new ListExpr(new IntegerNum(3), new IntegerNum(3), new IntegerNum(3), new IntegerNum(3), new IntegerNum(3)),
                new Table(new IntegerNum(3),
                        new ListExpr(new VarExpr("a"), new IntegerNum(5)))
                        .eval());

        // Table(x, a, 5) = {x,x,x,x,x}
        assertEquals(new ListExpr(new VarExpr("x"),new VarExpr("x"), new VarExpr("x"), new VarExpr("x"), new VarExpr("x")),
                new Table(new VarExpr("x"),
                        new ListExpr(new VarExpr("a"), new IntegerNum(5)))
                        .eval());

        // Table(a, a, 5) = {1,2,3,4,5}
        assertEquals(new ListExpr(new IntegerNum(1), new IntegerNum(2), new IntegerNum(3), new IntegerNum(4), new IntegerNum(5)),
                new Table(new VarExpr("a"),
                        new ListExpr(new VarExpr("a"), new IntegerNum(5)))
                        .eval());
    }

    @Test
    public void range_minToMax() {
        // Table(3, a, 5, 7) = {3,3,3}
        assertEquals(new ListExpr(new IntegerNum(3), new IntegerNum(3), new IntegerNum(3)),
                new Table(new IntegerNum(3),
                        new ListExpr(new VarExpr("a"), new IntegerNum(5), new IntegerNum(7)))
                        .eval());

        // Table(a, a, 5, 7) = {5,6,7}
        assertEquals(new ListExpr(new IntegerNum(5), new IntegerNum(6), new IntegerNum(7)),
                new Table(new VarExpr("a"),
                        new ListExpr(new VarExpr("a"), new IntegerNum(5), new IntegerNum(7)))
                        .eval());
    }

    @Test
    public void range_minToMax_step() {
        // Table(a, a, 2, 10, 3) = {2,5,8}
        assertEquals(new ListExpr(new IntegerNum(2), new IntegerNum(5), new IntegerNum(8)),
                new Table(new VarExpr("a"),
                        new ListExpr(new VarExpr("a"), new IntegerNum(2), new IntegerNum(10), new IntegerNum(3)))
                        .eval());
    }

    @Test
    public void range_values() {
        // Table(a, a, {3,6,1}} = {3,6,1}
        ListExpr values = new ListExpr(new IntegerNum(3), new IntegerNum(6), new IntegerNum(1));

        assertEquals(values,
                new Table(new VarExpr("a"), new ListExpr(new VarExpr("a"), values)).eval());
    }

    @Test
    public void exprDelayedEval() {
        Expr r = new RandomInteger();
        ListExpr params = new ListExpr(new IntegerNum(20));
        Expr e = new Table(r,params);

        e = e.eval();
        assertEquals(20, e.size());
        assertTrue(e.get(0).isInteger());
        IntegerNum sum = IntegerNum.ZERO;
        for(int i = 0; i < e.size(); i++)
            sum = sum.plus((IntegerNum)e.get(i));

        //Result should be list of 0s and 1s, verify there is at least a few of each
        assertTrue(sum.intValue() > 2);
        assertTrue(sum.intValue() < 18);
    }
}