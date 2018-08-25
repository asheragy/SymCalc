package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class TallyTest {

    @Test
    public void basic() {
        assertEquals(new ListExpr(new ListExpr(new VarExpr("a"), new IntegerNum(3))),
                new Tally(new ListExpr(new VarExpr("a"), new VarExpr("a"), new VarExpr("a"))).eval());

        Expr expected = new ListExpr(new ListExpr(new IntegerNum(5), new IntegerNum(2)), new ListExpr(new VarExpr("a"), new IntegerNum(1)), new ListExpr(new IntegerNum(2), new IntegerNum(1)));
        Expr actual = new Tally(new ListExpr(new IntegerNum(5), new VarExpr("a"), new IntegerNum(5), new IntegerNum(2))).eval();
        assertEquals(expected, actual);
    }
}