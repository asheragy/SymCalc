package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DotTest {

    @Test
    public void validate() {
        // Too few parameters
        Assert.assertTrue(new Dot(new ListExpr(new IntegerNum(1))).eval().isError());

        // Too many
        Assert.assertTrue(new Dot(
                new ListExpr(new IntegerNum(1)), new ListExpr(new IntegerNum(2)), new IntegerNum(3)
        ).eval().isError());

        // Not a list
        Assert.assertTrue(new Dot(
                new ListExpr(new IntegerNum(1)), new IntegerNum(2)
        ).eval().isError());

        // Vectors of unequal length
        Assert.assertTrue("Vectors must be same length", new Dot(
                new ListExpr(new IntegerNum(1)), new ListExpr(new IntegerNum(2), new IntegerNum(3))
        ).eval().isError());

        ListExpr vector2 = new ListExpr(new IntegerNum(1), new IntegerNum(2));
        ListExpr vector3 = new ListExpr(new IntegerNum(1), new IntegerNum(2), new IntegerNum(3));
        ListExpr matrix23 = new ListExpr(
                new ListExpr(new IntegerNum(1), new IntegerNum(2), new IntegerNum(3)),
                new ListExpr(new IntegerNum(4), new IntegerNum(5), new IntegerNum(6)));

        // Vector and matrix
        Assert.assertTrue("Arrays must be same rank (1)", new Dot(vector2, matrix23).eval().isError());
        Assert.assertTrue("Arrays must be same rank (2)", new Dot(vector3, matrix23).eval().isError());

        // Matrix of incompatible sizes
        Assert.assertTrue("Matrix of incompatible size", new Dot(matrix23, matrix23).eval().isError());
    }

    @Test
    public void vector() {
        Expr e = new Dot(
                new ListExpr(new IntegerNum(2), new IntegerNum(3)),
                new ListExpr(new IntegerNum(4), new IntegerNum(5)));

        Assert.assertEquals(new IntegerNum(23), e.eval());
    }

    @Test
    public void matrix() {
        Expr e = new Dot(
                new ListExpr(
                        new ListExpr(new IntegerNum(1), new IntegerNum(2), new IntegerNum(3)),
                        new ListExpr(new IntegerNum(4), new IntegerNum(5), new IntegerNum(6))),
                new ListExpr(
                        new ListExpr(new IntegerNum(7), new IntegerNum(8)),
                        new ListExpr(new IntegerNum(9), new IntegerNum(10)),
                        new ListExpr(new IntegerNum(11), new IntegerNum(12))
                        ));

        Expr expected = new ListExpr(
                new ListExpr(new IntegerNum(58), new IntegerNum(64)),
                new ListExpr(new IntegerNum(139), new IntegerNum(154))
        );

        Assert.assertEquals(expected, e.eval());
    }
}