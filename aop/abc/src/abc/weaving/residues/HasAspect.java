package abc.weaving.residues;

import java.util.*;
import soot.*;
import soot.util.Chain;
import soot.jimple.*;
import abc.soot.util.LocalGeneratorEx;
import abc.weaving.weaver.WeavingContext;

/** A residue that puts the relevant aspect instance into a 
 * local variable in the weaving context
 *  @author Ganesh Sittampalam
 */ 

public class HasAspect extends Residue {

    private SootClass aspect;

    // null to indicate singleton aspect; i.e. no params to hasAspect
    // (probably not actually used, but kept for symmetry with AspectOf)
    private ContextValue pervalue;

    public HasAspect(SootClass aspect,ContextValue pervalue) {
	this.aspect=aspect;
	this.pervalue=pervalue;
    }

    public Stmt codeGen(SootMethod method,LocalGeneratorEx localgen,
			Chain units,Stmt begin,Stmt fail,
			WeavingContext wc) {

       	List paramTypes;
	List params;
	if(pervalue==null) {
	    params=new ArrayList(); paramTypes=new ArrayList();
	} else {
	    params=new ArrayList(1); paramTypes=new ArrayList(1);
	    paramTypes.add(Scene.v().getSootClass("java.lang.Object").getType());
	    params.add(pervalue.getSootValue());
	}

	Local hasaspect = localgen.generateLocal(BooleanType.v(),"hasAspect");
	AssignStmt stmtHasAspect = Jimple.v().newAssignStmt
	    (hasaspect, Jimple.v().newStaticInvokeExpr
	     (aspect.getMethodByName("hasAspect"),params));

	units.insertAfter(stmtHasAspect,begin);

	Stmt abort=Jimple.v().newIfStmt
	    (Jimple.v().newEqExpr(hasaspect,IntConstant.v(0)),fail);
	units.insertAfter(abort,stmtHasAspect);
	return abort;
    }

    public String toString() {
	return "aspectof("+aspect+","+pervalue+")";
    }

}
