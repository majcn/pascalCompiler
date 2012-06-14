package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Deklaracija spremenljivke.
 */
public class AbsVarDecl extends AbsDecl {

	/** Ime spremenljivke. */
	public AbsDeclName name;
	
	/** Tip spremenljivke. */
	public AbsTypeExpr type;
	
	public boolean isPrivate;
	
	public AbsVarDecl(AbsDeclName name, AbsTypeExpr type, boolean isPrivate) {
		this.name = name;
		this.type = type;
		this.isPrivate = isPrivate;
	}
	
	public AbsVarDecl(AbsDeclName name, AbsTypeExpr type) {
		this.name = name;
		this.type = type;
		this.isPrivate = false;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
