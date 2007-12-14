
package AST;
import java.util.HashSet;import java.util.LinkedHashSet;import java.io.FileNotFoundException;import java.io.File;import java.util.*;import beaver.*;import java.util.ArrayList;import java.util.zip.*;import java.io.*;import sun.text.normalizer.UTF16;import changes.*;import main.FileRange;


public abstract class BodyDecl extends ASTNode implements Cloneable,  Scope {
    public void flushCache() {
        super.flushCache();
        isDAafter_Variable_values = null;
        isDUafter_Variable_values = null;
        isDAbefore_Variable_values = null;
        isDUbefore_Variable_values = null;
        typeThrowable_computed = false;
        typeThrowable_value = null;
        lookupVariable_String_values = null;
        accessMethod_MethodDecl_List_values = null;
    }
    public Object clone() throws CloneNotSupportedException {
        BodyDecl node = (BodyDecl)super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.isDAbefore_Variable_values = null;
        node.isDUbefore_Variable_values = null;
        node.typeThrowable_computed = false;
        node.typeThrowable_value = null;
        node.lookupVariable_String_values = null;
        node.accessMethod_MethodDecl_List_values = null;
        node.in$Circle(false);
        node.is$Final(false);
    return node;
    }
    // Declared in BranchTarget.jrag at line 202

  public void collectFinally(Stmt branchStmt, ArrayList list) {
    // terminate search if body declaration is reached
  }

    // Declared in Scope.jadd at line 13

	public TypeDecl surroundingType() { return hostType(); }

    // Declared in java.ast at line 3
    // Declared in java.ast line 68

    public BodyDecl() {
        super();


    }

    // Declared in java.ast at line 9


  protected int numChildren() {
    return 0;
  }

    // Declared in java.ast at line 12

  public boolean mayHaveRewrite() { return false; }

    protected java.util.Map isDAafter_Variable_values;
    // Declared in DefiniteAssignment.jrag at line 234
    public boolean isDAafter(Variable v) {
        Object _parameters = v;
if(isDAafter_Variable_values == null) isDAafter_Variable_values = new java.util.HashMap(4);
        if(isDAafter_Variable_values.containsKey(_parameters))
            return ((Boolean)isDAafter_Variable_values.get(_parameters)).booleanValue();
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        boolean isDAafter_Variable_value = isDAafter_compute(v);
        if(isFinal && num == boundariesCrossed)
            isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
        return isDAafter_Variable_value;
    }

    private boolean isDAafter_compute(Variable v) {  return  true;  }

    protected java.util.Map isDUafter_Variable_values;
    // Declared in DefiniteAssignment.jrag at line 705
    public boolean isDUafter(Variable v) {
        Object _parameters = v;
if(isDUafter_Variable_values == null) isDUafter_Variable_values = new java.util.HashMap(4);
        if(isDUafter_Variable_values.containsKey(_parameters))
            return ((Boolean)isDUafter_Variable_values.get(_parameters)).booleanValue();
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        boolean isDUafter_Variable_value = isDUafter_compute(v);
        if(isFinal && num == boundariesCrossed)
            isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
        return isDUafter_Variable_value;
    }

    private boolean isDUafter_compute(Variable v) {  return  true;  }

    // Declared in LookupType.jrag at line 333
    public boolean declaresType(String name) {
        boolean declaresType_String_value = declaresType_compute(name);
        return declaresType_String_value;
    }

    private boolean declaresType_compute(String name) {  return  false;  }

    // Declared in LookupType.jrag at line 335
    public TypeDecl type(String name) {
        TypeDecl type_String_value = type_compute(name);
        return type_String_value;
    }

    private TypeDecl type_compute(String name) {  return  null;  }

    // Declared in TypeAnalysis.jrag at line 273
    public boolean isVoid() {
        boolean isVoid_value = isVoid_compute();
        return isVoid_value;
    }

    private boolean isVoid_compute() {  return  false;  }

    // Declared in ControlFlowGraph.jrag at line 330
    public boolean hasControlFlow() {
        boolean hasControlFlow_value = hasControlFlow_compute();
        return hasControlFlow_value;
    }

    private boolean hasControlFlow_compute() {  return  false;  }

    // Declared in ControlFlowGraph.jrag at line 336
    public Stmt exitBlock() {
        Stmt exitBlock_value = exitBlock_compute();
        return exitBlock_value;
    }

    private Stmt exitBlock_compute() {  return  null;  }

    // Declared in ControlFlowGraph.jrag at line 342
    public Stmt entryBlock() {
        Stmt entryBlock_value = entryBlock_compute();
        return entryBlock_value;
    }

    private Stmt entryBlock_compute() {  return  null;  }

    // Declared in AccessType.jrag at line 26
    public Access accessType(TypeDecl td, boolean ambiguous) {
        Access accessType_TypeDecl_boolean_value = accessType_compute(td, ambiguous);
        return accessType_TypeDecl_boolean_value;
    }

    private Access accessType_compute(TypeDecl td, boolean ambiguous)  {
		String name = td.getID();
		SimpleSet set = lookupType(name);
		if(isSingletonOf(set, td) && !(ambiguous && !lookupVariable(name).isEmpty()))
			return new TypeAccess(name);
		TypeDecl out = surroundingType().findTypeOutwards(td, ambiguous);
		if(out != null) {
			Access outacc = accessType(out, ambiguous);
			if(outacc != null) return outacc.qualifiesAccess(new TypeAccess(name));
		}
		if(td.isTopLevelType()) {
			String pkg = td.packageName();
			String first_component = pkg.split("\\.")[0];
			if(!(ambiguous && (!lookupVariable(first_component).isEmpty() ||
							 !lookupType(first_component).isEmpty())))
				return new TypeAccess(pkg, td.getID());
		}
		return null;
	}

    protected java.util.Map isDAbefore_Variable_values;
    // Declared in DefiniteAssignment.jrag at line 233
    public boolean isDAbefore(Variable v) {
        Object _parameters = v;
if(isDAbefore_Variable_values == null) isDAbefore_Variable_values = new java.util.HashMap(4);
        if(isDAbefore_Variable_values.containsKey(_parameters))
            return ((Boolean)isDAbefore_Variable_values.get(_parameters)).booleanValue();
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);
        if(isFinal && num == boundariesCrossed)
            isDAbefore_Variable_values.put(_parameters, Boolean.valueOf(isDAbefore_Variable_value));
        return isDAbefore_Variable_value;
    }

    protected java.util.Map isDUbefore_Variable_values;
    // Declared in DefiniteAssignment.jrag at line 704
    public boolean isDUbefore(Variable v) {
        Object _parameters = v;
if(isDUbefore_Variable_values == null) isDUbefore_Variable_values = new java.util.HashMap(4);
        if(isDUbefore_Variable_values.containsKey(_parameters))
            return ((Boolean)isDUbefore_Variable_values.get(_parameters)).booleanValue();
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
        if(isFinal && num == boundariesCrossed)
            isDUbefore_Variable_values.put(_parameters, Boolean.valueOf(isDUbefore_Variable_value));
        return isDUbefore_Variable_value;
    }

    protected boolean typeThrowable_computed = false;
    protected TypeDecl typeThrowable_value;
    // Declared in ExceptionHandling.jrag at line 13
    public TypeDecl typeThrowable() {
        if(typeThrowable_computed)
            return typeThrowable_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeThrowable_value = getParent().Define_TypeDecl_typeThrowable(this, null);
        if(isFinal && num == boundariesCrossed)
            typeThrowable_computed = true;
        return typeThrowable_value;
    }

    // Declared in LookupType.jrag at line 87
    public TypeDecl lookupType(String packageName, String typeName) {
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    protected java.util.Map lookupVariable_String_values;
    // Declared in LookupVariable.jrag at line 6
    public SimpleSet lookupVariable(String name) {
        Object _parameters = name;
if(lookupVariable_String_values == null) lookupVariable_String_values = new java.util.HashMap(4);
        if(lookupVariable_String_values.containsKey(_parameters))
            return (SimpleSet)lookupVariable_String_values.get(_parameters);
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        if(isFinal && num == boundariesCrossed)
            lookupVariable_String_values.put(_parameters, lookupVariable_String_value);
        return lookupVariable_String_value;
    }

    // Declared in TypeAnalysis.jrag at line 560
    public String hostPackage() {
        String hostPackage_value = getParent().Define_String_hostPackage(this, null);
        return hostPackage_value;
    }

    // Declared in TypeAnalysis.jrag at line 575
    public TypeDecl hostType() {
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    // Declared in AccessField.jrag at line 84
    public Access accessField(FieldDeclaration fd) {
        Access accessField_FieldDeclaration_value = getParent().Define_Access_accessField(this, null, fd);
        return accessField_FieldDeclaration_value;
    }

    protected java.util.Map accessMethod_MethodDecl_List_values;
    // Declared in AccessMethod.jrag at line 6
    public Access accessMethod(MethodDecl md, List args) {
        java.util.List _parameters = new java.util.ArrayList(2);
        _parameters.add(md);
        _parameters.add(args);
if(accessMethod_MethodDecl_List_values == null) accessMethod_MethodDecl_List_values = new java.util.HashMap(4);
        if(accessMethod_MethodDecl_List_values.containsKey(_parameters))
            return (Access)accessMethod_MethodDecl_List_values.get(_parameters);
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        Access accessMethod_MethodDecl_List_value = getParent().Define_Access_accessMethod(this, null, md, args);
        if(isFinal && num == boundariesCrossed)
            accessMethod_MethodDecl_List_values.put(_parameters, accessMethod_MethodDecl_List_value);
        return accessMethod_MethodDecl_List_value;
    }

    // Declared in AccessPackage.jrag at line 4
    public Access accessPackage(String pkg) {
        Access accessPackage_String_value = getParent().Define_Access_accessPackage(this, null, pkg);
        return accessPackage_String_value;
    }

    // Declared in AccessPackage.jrag at line 12
    public SimpleSet lookupType(String name) {
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        return lookupType_String_value;
    }

public ASTNode rewriteTo() {
    return super.rewriteTo();
}

}
