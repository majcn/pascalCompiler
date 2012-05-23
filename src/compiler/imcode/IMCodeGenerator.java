package compiler.imcode;

import java.util.LinkedList;

import compiler.abstree.AbsVisitor;
import compiler.abstree.tree.*;

public class IMCodeGenerator implements AbsVisitor {

	public LinkedList<ImcChunk> chunks;
	private boolean debug = true;
	
	private ImcCode result;
	private ImcCode getResult() {
		ImcCode temp = result;
		result = null;
		return temp;
	}
	private void setResult(ImcCode result) {
		this.result = result;
	}

	@Override
	public void visit(AbsAlloc acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		int value = 0;
		switch (acceptor.type) {
		case AbsAtomConst.BOOL:
			value = (acceptor.value.equals("true")) ? 1 : 0;
			break;
		case AbsAtomConst.CHAR:
			value = (int)acceptor.value.charAt(1);
			break;
		case AbsAtomConst.INT:
			value = Integer.parseInt(acceptor.value);
			break;
		}
		setResult(new ImcCONST(value));
		if(debug) System.out.println("AbsAtomConst: type="+acceptor.type+", value="+acceptor.value+", result="+value);
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		acceptor.fstExpr.accept(this);
		ImcExpr limc = (ImcExpr) getResult();
		
		acceptor.sndExpr.accept(this);
		ImcExpr rimc = (ImcExpr) getResult();
		
		setResult(new ImcBINOP(acceptor.oper, limc, rimc));
		//TODO array in dot
	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
		// TODO Auto-generated method stub
		acceptor.value.accept(this);
		
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsDecls acceptor) {
		for (AbsDecl decl : acceptor.decls) {
			decl.accept(this);
		}
	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsNilConst acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsProgram acceptor) {
		// TODO Auto-generated method stub
		acceptor.decls.accept(this);
		
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsStmts acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsValName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		// TODO Auto-generated method stub
		
	}

}
