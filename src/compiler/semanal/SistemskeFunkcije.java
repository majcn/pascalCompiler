package compiler.semanal;

import compiler.abstree.tree.AbsAtomType;
import compiler.abstree.tree.AbsDecl;
import compiler.abstree.tree.AbsDeclName;
import compiler.abstree.tree.AbsDecls;
import compiler.abstree.tree.AbsFunDecl;
import compiler.abstree.tree.AbsProcDecl;
import compiler.abstree.tree.AbsVarDecl;
import compiler.semanal.type.SemAtomType;

public class SistemskeFunkcije {
	
	private static final String[] funkcije = { "chr" };
	private static final String[] procedure = { "putch", "putint" };
	public static final int FAKE_FP = 2011988;
	
	public static void fillData() {
		AbsDeclName name = null;
		AbsDecls pars = null;
		AbsDecl par = null;
		
		//putch
		name = new AbsDeclName("putch");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.CHAR));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.CHAR));
		pars.decls.add(par);
		try {
			SemTable.ins(name.name, new AbsProcDecl(name, pars, null, null));
		} catch (SemIllegalInsertException e1) {}
		
		//chr
		name = new AbsDeclName("chr");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.INT));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.INT));
		pars.decls.add(par);
		try {
			SemTable.ins(name.name, new AbsFunDecl(name, pars, new AbsAtomType(AbsAtomType.CHAR) ,null, null));
		} catch (SemIllegalInsertException e1) {}
		
		//putint
		name = new AbsDeclName("putint");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.INT));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.INT));
		pars.decls.add(par);
		try {
			SemTable.ins(name.name, new AbsProcDecl(name, pars, null, null));
		} catch (SemIllegalInsertException e1) {}
	}
	
	public static boolean isFunction(String name) {
		for(String s:funkcije) {
			if(s.equals(name)) return true;
		}
		return false;
	}
	
	public static boolean isProcedure(String name) {
		for(String s:procedure) {
			if(s.equals(name)) return true;
		}
		return false;
	}
	

}
