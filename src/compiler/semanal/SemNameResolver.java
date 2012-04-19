package compiler.semanal;

import compiler.abstree.AbsVisitor;
import compiler.abstree.tree.*;
import compiler.report.Report;

public class SemNameResolver implements AbsVisitor {
	
	protected boolean error = false;
	private boolean debug = false;
	
	public void warningMsgRedefined(int line, String name) {
		Report.warning(String.format("line %d: '%s' is redefined", line, name));
		error = true;
	}
	
	public void warningMsgUndefined(int line, String name) {
		Report.warning(String.format("line %d: '%s' is undefined", line, name));
		error = true;
	}

	@Override
	public void visit(AbsAlloc acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAlloc");
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsArrayType");
		acceptor.type.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAssignStmt");
		acceptor.srcExpr.accept(this);
		acceptor.dstExpr.accept(this);
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAtomConst");
		//empty visitor
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAtomType");
		//empty visitor
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsBinExpr");
		acceptor.fstExpr.accept(this);
		acceptor.sndExpr.accept(this);
	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsBlockStmt");
		acceptor.stmts.accept(this);
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsCallExpr");
		acceptor.name.accept(this);
		acceptor.args.accept(this);
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsConstDecl");
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			warningMsgRedefined(acceptor.begLine, acceptor.name.name);
		}
		acceptor.value.accept(this);
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsDeclName");
		if(SemTable.fnd(acceptor.name) == null)
			warningMsgUndefined(acceptor.begLine, acceptor.name);
	}

	@Override
	public void visit(AbsDecls acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsDecls");
		for(AbsDecl e: acceptor.decls) {
			e.accept(this);
		}			
	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsExprStmt");
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsForStmt");
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsFunDecl");
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			warningMsgRedefined(acceptor.begLine, acceptor.name.name);
		}
		SemTable.newScope();
			try {
				SemTable.ins(acceptor.name.name, acceptor);
			} catch (SemIllegalInsertException e) {}		
			acceptor.pars.accept(this);
			acceptor.type.accept(this);
			acceptor.decls.accept(this);
			acceptor.stmt.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsIfStmt");
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
	}

	@Override
	public void visit(AbsNilConst acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsNilConst");
		//empty visitor
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsPointerType");
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsProcDecl");
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			warningMsgRedefined(acceptor.begLine, acceptor.name.name);
		}		
		SemTable.newScope();
			try {
				SemTable.ins(acceptor.name.name, acceptor);
			} catch (SemIllegalInsertException e) {}		
			acceptor.pars.accept(this);
			acceptor.decls.accept(this);
			acceptor.stmt.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsProgram acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsProgram");
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsRecordType");
		SemTable.newScope();
			acceptor.fields.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsStmts acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsStmts");
		for(AbsStmt e: acceptor.stmts)
			e.accept(this);
	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsTypeDecl");
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			warningMsgRedefined(acceptor.begLine, acceptor.name.name);
		}
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsTypeName");
		if(SemTable.fnd(acceptor.name) == null)
			warningMsgUndefined(acceptor.begLine, acceptor.name);
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsUnExpr");
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsValExprs");
		for(AbsValExpr e: acceptor.exprs)
			e.accept(this);
	}

	@Override
	public void visit(AbsValName acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsValName");
		if(SemTable.fnd(acceptor.name) == null)
			warningMsgUndefined(acceptor.begLine, acceptor.name);
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsVarDecl");
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			warningMsgRedefined(acceptor.begLine, acceptor.name.name);
		}
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsWhileStmt");
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
	}
	
}