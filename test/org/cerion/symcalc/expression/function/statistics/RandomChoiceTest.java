package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomChoiceTest {

    @Test
    public void basic() {
        VarExpr a = new VarExpr("a");
        VarExpr b = new VarExpr("b");
        IntegerNum c = new IntegerNum(6);

        Expr e = new RandomChoice(new ListExpr(a, b, c));
        boolean found[] = { false, false, false };
        for (int i = 0; i < 100; i++) {
            Expr eval = e.eval();

            if (eval.equals(a))
                found[0] = true;
            else if (eval.equals(b))
                found[1] = true;
            else if (eval.equals(c))
                found[2] = true;

            if (found[0] && found[1] && found[2])
                return;
        }

        assertFalse(true);
    }

    @Test
    public void basicList() {
        VarExpr a = new VarExpr("a");
        VarExpr b = new VarExpr("b");
        IntegerNum c = new IntegerNum(6);
        Expr e = new RandomChoice(new ListExpr(a, b, c), new IntegerNum(100));
        e = e.eval();

        assertTrue(e.isList());
        ListExpr list = (ListExpr)e;
        assertEquals(100, list.size());

        boolean found[] = { false, false, false };
        for (int i = 0; i < 100; i++) {
            Expr curr = list.get(i);

            if (curr.equals(a))
                found[0] = true;
            else if (curr.equals(b))
                found[1] = true;
            else if (curr.equals(c))
                found[2] = true;
        }

        assertTrue(found[0] && found[1] && found[2]);
    }
}