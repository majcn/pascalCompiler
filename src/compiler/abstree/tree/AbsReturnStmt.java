package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * stavek 'return'.
 */
public class AbsReturnStmt extends AbsStmt {
	
	public AbsReturnStmt() {
	}
	
	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
