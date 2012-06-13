package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Stavek 'repeat until'.
 */
public class AbsRepeatStmt extends AbsStmt {

	/** Stavek. */
	public AbsStmt stmt;
	
	/** Pogoj. */
	public AbsValExpr cond;
	
	public AbsRepeatStmt(AbsStmt stmt, AbsValExpr cond) {
		this.stmt = stmt;
		this.cond = cond;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
