package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartitionTest {

    @Test
    public void validate() {

    }

    @Test
    public void basicTest() {
        // Same length
        ListExpr list = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, new IntegerNum(3));
        Expr e = new Partition(list, new IntegerNum(4)).eval();
        assertEquals(new ListExpr(list), e);

        // Split in half
        e = new Partition(list, IntegerNum.TWO).eval();

        ListExpr expected = new ListExpr(
                new ListExpr(IntegerNum.ZERO, IntegerNum.ONE),
                new ListExpr(IntegerNum.TWO, new IntegerNum(3)));

        assertEquals(expected, e);
    }

    @Test
    public void listTooShort() {
        // Simpler version of next test where the first sublist is too short

        ListExpr list = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO);
        Expr e = new Partition(list, new IntegerNum(4)).eval();

        ListExpr expected = new ListExpr();
        assertEquals(expected, e);
    }

    @Test
    public void truncateExtraItems() {
        ListExpr list = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, new IntegerNum(3), new IntegerNum(4));
        Expr e = new Partition(list, new IntegerNum(2)).eval();

        ListExpr expected = new ListExpr(
                new ListExpr(IntegerNum.ZERO, IntegerNum.ONE),
                new ListExpr(IntegerNum.TWO, new IntegerNum(3)));

        assertEquals(expected, e);
    }
}