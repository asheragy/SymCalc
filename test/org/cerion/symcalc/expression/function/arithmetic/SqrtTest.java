package org.cerion.symcalc.expression.function.arithmetic;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.junit.Test;

import static org.junit.Assert.*;


public class SqrtTest {

    @Test
    public void validate() {
        assertEquals(Expr.ExprType.ERROR, new Sqrt().eval().getType());
        assertEquals(Expr.ExprType.ERROR, new Sqrt(new IntegerNum(1), new IntegerNum(2)).eval().getType());
    }

    @Test
    public void basic() {
        // Converts to power
        assertEquals(new Power(new IntegerNum(3), new RationalNum(1,2)), new Sqrt(new IntegerNum(3)).eval());

        // Evaluates when square number
        assertEquals(new IntegerNum(3), new Sqrt(new IntegerNum(9)).eval());
        assertEquals(new IntegerNum(4), new Sqrt(new IntegerNum(16)).eval());
        assertEquals(new IntegerNum("985236412054"), new Sqrt(new IntegerNum("970690787637039276498916")).eval());
    }

    @Test
    public void reduces() {
        // 8 = 2*sqrt(2)
        assertEquals(new Times(new IntegerNum(2), new Power(new IntegerNum(2), new RationalNum(1,2))),
                new Sqrt(new IntegerNum(8)).eval());

        // 497664 = 288 * sqrt(6)
        assertEquals(new Times(new IntegerNum(288), new Power(new IntegerNum(6), new RationalNum(1,2))),
                new Sqrt(new IntegerNum(497664)).eval());
    }
}