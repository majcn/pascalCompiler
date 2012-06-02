package compiler.semanal;

import compiler.abstree.tree.AbsAtomType;
import compiler.abstree.tree.AbsDecl;
import compiler.abstree.tree.AbsDeclName;
import compiler.abstree.tree.AbsDecls;
import compiler.abstree.tree.AbsFunDecl;
import compiler.abstree.tree.AbsPointerType;
import compiler.abstree.tree.AbsProcDecl;
import compiler.abstree.tree.AbsTypeExpr;
import compiler.abstree.tree.AbsVarDecl;
import compiler.semanal.type.SemAtomType;
import compiler.semanal.type.SemPointerType;

public class SistemskeFunkcije {
	
	private static final String[] funkcije = { "chr", "getch", "getint", "ord", "chr" };
	private static final String[] procedure = { "putch", "putint", "free" };
	public static final int FAKE_FP = 2011988;
	
	public static void fillData() {
		AbsDeclName name = null;
		AbsDecls pars = null;
		AbsDecl par = null;
		AbsTypeExpr type = null;
		
		//putch
		name = new AbsDeclName("putch");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.CHAR));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.CHAR));
		pars.decls.add(par);
		try {
			SemTable.ins(name.name, new AbsProcDecl(name, pars, null, null));
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
		
		//getch
		name = new AbsDeclName("getch");
		pars = new AbsDecls();
		type = new AbsAtomType(AbsAtomType.CHAR);
		SemDesc.setActualType(type, new SemAtomType(SemAtomType.CHAR));
		try {
			SemTable.ins(name.name, new AbsFunDecl(name, pars, type ,null, null));
		} catch (SemIllegalInsertException e1) {}
		
		//getint
		name = new AbsDeclName("getint");
		pars = new AbsDecls();
		type = new AbsAtomType(AbsAtomType.INT);
		SemDesc.setActualType(type, new SemAtomType(SemAtomType.INT));
		try {
			SemTable.ins(name.name, new AbsFunDecl(name, pars, type ,null, null));
		} catch (SemIllegalInsertException e1) {}
		
		//ord
		name = new AbsDeclName("ord");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.CHAR));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.CHAR));
		pars.decls.add(par);
		type = new AbsAtomType(AbsAtomType.INT);
		SemDesc.setActualType(type, new SemAtomType(SemAtomType.INT));
		try {
			SemTable.ins(name.name, new AbsFunDecl(name, pars, type ,null, null));
		} catch (SemIllegalInsertException e1) {}

		//chr
		name = new AbsDeclName("chr");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsAtomType(AbsAtomType.INT));
		SemDesc.setActualType(par, new SemAtomType(SemAtomType.INT));
		pars.decls.add(par);
		type = new AbsAtomType(AbsAtomType.CHAR);
		SemDesc.setActualType(type, new SemAtomType(SemAtomType.CHAR));
		try {
			SemTable.ins(name.name, new AbsFunDecl(name, pars, type ,null, null));
		} catch (SemIllegalInsertException e1) {}
		
		//free: treba je ignorirat typecheck pri call
		name = new AbsDeclName("free");
		pars = new AbsDecls();
		par = new AbsVarDecl(name, new AbsPointerType(new AbsAtomType(AbsAtomType.CHAR))); //en fejk pointer (recimo na char)
		SemDesc.setActualType(par, new SemPointerType(new SemAtomType(SemAtomType.CHAR)));
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
