package compiler.semanal;

import java.util.HashMap;

import compiler.abstree.AbsVisitor;
import compiler.abstree.tree.*;
import compiler.semanal.type.*;
import compiler.report.Report;

public class SemTypeChecker implements AbsVisitor {
	
	protected boolean error = false;
	private boolean debug = false;
	
	private SemType typeInt = new SemAtomType(SemAtomType.INT);
	private SemType typeBool = new SemAtomType(SemAtomType.BOOL);
	private SemType typeChar = new SemAtomType(SemAtomType.CHAR);
	private SemType typeVoid = new SemAtomType(SemAtomType.VOID);
	
	private HashMap<Integer, SemRecordType> records = new HashMap<Integer, SemRecordType>();
	private Integer record = 0;
	private boolean notRecord() {
		return record == 0;
	}
	
	public void warningMsgWrongType(int line, String oper) {
		Report.warning(String.format("line %d: wrong type near %s", line, oper));
		error = true;
	}

	@Override
	public void visit(AbsAlloc acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAlloc");
		acceptor.type.accept(this);
		
		SemType a = SemDesc.getActualType(acceptor.type);
		if(a.coercesTo(typeInt)) {
			SemDesc.setActualType(acceptor, typeInt);
		} else {
			warningMsgWrongType(acceptor.begLine, "[]");
		}
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsArrayType");
		acceptor.type.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		
		SemType a = SemDesc.getActualType(acceptor.loBound);
		SemType b = SemDesc.getActualType(acceptor.hiBound);
		if(a.coercesTo(typeInt) && b.coercesTo(typeInt)) {
			SemDesc.setActualType(acceptor, new SemArrayType(SemDesc.getActualType(acceptor.type), SemDesc.getActualConst(acceptor.loBound), SemDesc.getActualConst(acceptor.hiBound)));
		} else {
			warningMsgWrongType(acceptor.begLine, "ARRAY");
		}
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAssignStmt");
		acceptor.srcExpr.accept(this);
		acceptor.dstExpr.accept(this);
		
		SemType a = SemDesc.getActualType(acceptor.srcExpr);
		SemType b = SemDesc.getActualType(acceptor.dstExpr);
		if(!a.coercesTo(b) || !(a instanceof SemAtomType || a instanceof SemPointerType)) {
			warningMsgWrongType(acceptor.begLine, ":=");
		}
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAtomConst");
		
		switch (acceptor.type) {
		case AbsAtomConst.BOOL:
			SemDesc.setActualType(acceptor, typeBool);
			break;
		case AbsAtomConst.CHAR:
			SemDesc.setActualType(acceptor, typeChar);
			break;
		case AbsAtomConst.INT:
			SemDesc.setActualType(acceptor, typeInt);
			break;
		}
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsAtomType");
		
		switch (acceptor.type) {
		case AbsAtomConst.BOOL:
			SemDesc.setActualType(acceptor, typeBool);
			break;
		case AbsAtomConst.CHAR:
			SemDesc.setActualType(acceptor, typeChar);
			break;
		case AbsAtomConst.INT:
			SemDesc.setActualType(acceptor, typeInt);
			break;
		}
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsBinExpr");
		acceptor.fstExpr.accept(this);
		acceptor.sndExpr.accept(this);
		
		String warningOper = "";
		SemType a = SemDesc.getActualType(acceptor.fstExpr);
		SemType b = SemDesc.getActualType(acceptor.sndExpr);
		switch (acceptor.oper) {
		case AbsBinExpr.ADD:
			if (warningOper == "") warningOper = "+";
		case AbsBinExpr.SUB:
			if (warningOper == "") warningOper = "-";
		case AbsBinExpr.MUL:
			if (warningOper == "") warningOper = "*";
		case AbsBinExpr.DIV:
			if (warningOper == "") warningOper = "/";
			if(a.coercesTo(typeInt) && b.coercesTo(typeInt)) {
				SemDesc.setActualType(acceptor, typeInt);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		case AbsBinExpr.AND:
			if (warningOper == "") warningOper = "AND";
		case AbsBinExpr.OR:
			if (warningOper == "") warningOper = "OR";
			if(a.coercesTo(typeBool) && b.coercesTo(typeBool)) {
				SemDesc.setActualType(acceptor, typeBool);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		case AbsBinExpr.EQU:
			if (warningOper == "") warningOper = "=";
		case AbsBinExpr.NEQ:
			if (warningOper == "") warningOper = "<>";
		case AbsBinExpr.LTH:
			if (warningOper == "") warningOper = "<";
		case AbsBinExpr.GTH:
			if (warningOper == "") warningOper = ">";
		case AbsBinExpr.LEQ:
			if (warningOper == "") warningOper = "<=";
		case AbsBinExpr.GEQ:
			if (warningOper == "") warningOper = ">=";
			if(a.coercesTo(b) && (a instanceof SemAtomType || a instanceof SemPointerType)) {
				SemDesc.setActualType(acceptor, typeBool);	
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		case AbsBinExpr.ARRACCESS:
			if(a instanceof SemArrayType && b.coercesTo(typeInt)) {
				SemDesc.setActualType(acceptor, ((SemArrayType)a).type);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		}
		//TODO record
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
		acceptor.value.accept(this);
		
		SemDesc.setActualType(acceptor, SemDesc.getActualType(acceptor.value));
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		if(debug) System.out.println("How did u get there oO!!!");
		//never visited
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
		
		SemType a = SemDesc.getActualType(acceptor.name);
		SemType b = SemDesc.getActualType(acceptor.loBound);
		SemType c = SemDesc.getActualType(acceptor.hiBound);
		
		if(a.coercesTo(typeInt) && b.coercesTo(typeInt) && c.coercesTo(typeInt))
			warningMsgWrongType(acceptor.begLine, "FOR");
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsFunDecl");
		acceptor.pars.accept(this);
		acceptor.type.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsIfStmt");
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
		
		SemType a = SemDesc.getActualType(acceptor.cond);
		if(!a.coercesTo(typeBool))
			warningMsgWrongType(acceptor.begLine, "IF");
	}

	@Override
	public void visit(AbsNilConst acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsNilConst");
		
		SemDesc.setActualType(acceptor, typeVoid);
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsPointerType");
		acceptor.type.accept(this);
		
		SemDesc.setActualType(acceptor, new SemPointerType(SemDesc.getActualType(acceptor.type)));
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsProcDecl");
//		SemTable.newScope();
			acceptor.pars.accept(this);
			acceptor.decls.accept(this);
			acceptor.stmt.accept(this);
//		SemTable.oldScope();
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
		record++;		
		records.put(record, new SemRecordType());
		acceptor.fields.accept(this);
		SemDesc.setActualType(acceptor, records.get(record));
		record--;
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
		acceptor.type.accept(this);
		
		if(notRecord())
			SemDesc.setActualType(acceptor, SemDesc.getActualType(acceptor.type));
		else
			records.get(record).addField(acceptor.name, SemDesc.getActualType(acceptor.type));
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsTypeName");
		
		SemDesc.setActualType(acceptor, SemDesc.getActualType(SemDesc.getNameDecl(acceptor)));
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsUnExpr");
		acceptor.expr.accept(this);
		
		String warningOper = "";
		SemType a = SemDesc.getActualType(acceptor.expr);
		switch (acceptor.oper) {
		case AbsUnExpr.NOT:
			if (warningOper == "") warningOper = "NOT";
			if(a.coercesTo(typeBool)) {
				SemDesc.setActualType(acceptor, typeBool);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		case AbsUnExpr.ADD:
			if (warningOper == "") warningOper = "+";
		case AbsUnExpr.SUB:
			if (warningOper == "") warningOper = "-";
			if(a.coercesTo(typeInt)) {
				SemDesc.setActualType(acceptor, typeInt);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		case AbsUnExpr.MEM:
			SemDesc.setActualType(acceptor, new SemPointerType(a));
			break;
		case AbsUnExpr.VAL:
			if (warningOper == "") warningOper = "^";
			if(a instanceof SemPointerType) {
				SemDesc.setActualType(acceptor, ((SemPointerType)a).type);
			} else {
				warningMsgWrongType(acceptor.begLine, warningOper);
			}
			break;
		}
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
		
		SemDesc.setActualType(acceptor, SemDesc.getActualType(SemDesc.getNameDecl(acceptor)));
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsVarDecl");
		acceptor.type.accept(this);
		
		SemDesc.setActualType(acceptor, SemDesc.getActualType(acceptor.type));
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		if(debug) System.out.println(acceptor.begLine + " AbsWhileStmt");
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
		
		SemType a = SemDesc.getActualType(acceptor.cond);
		if (!a.coercesTo(typeBool)) {
			warningMsgWrongType(acceptor.begLine, "WHILE");
		}
	}

}