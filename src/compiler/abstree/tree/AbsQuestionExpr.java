package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

public class AbsQuestionExpr extends AbsValExpr {
	
	public AbsValExpr exprCond;
	public AbsValExpr exprTrue;
	public AbsValExpr exprFalse;
	
	public AbsQuestionExpr(AbsValExpr exprCond, AbsValExpr exprTrue, AbsValExpr exprFalse) {
		this.exprCond = exprCond;
		this.exprTrue = exprTrue;
		this.exprFalse = exprFalse;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}
}
