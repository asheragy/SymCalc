package org.cerion.symcalc.expression;

public class GraphicsExpr extends Expr {

    @Override
    public String toString() {
        return "Graphics";
    }

    @Override
    public void show(int i) {
        System.out.println("Graphics");
    }

    @Override
    public boolean equals(Expr e) {
        return false;
    }

    @Override
    protected Expr evaluate() {
        return this;
    }

    @Override
    protected ExprType getType() {
        return ExprType.GRAPHICS;
    }
}
