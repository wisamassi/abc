
package abc.aspectj.visit;

import abc.aspectj.ast.*;

import polyglot.ast.*;
import polyglot.visit.*;

import java.util.*;
import java.util.regex.*;

import abc.weaving.aspectinfo.MethodCategory;

import soot.*;

public class PatternMatcher {
    private PCStructure hierarchy;
    private Map/*<NamePattern,Set<PCNode>>*/ pattern_matches = new HashMap();
    private Map/*<NamePattern,Set<String>>*/ pattern_classes = new HashMap();
    private Map/*<NamePattern,Set<String>>*/ pattern_packages = new HashMap();
    private Map/*<NamePattern,PCNode>*/ pattern_context = new HashMap();

    private Set/*<String>*/ prim_types;

    private Map/*<String,Pattern>*/ name_pattern_cache = new HashMap();

    private static PatternMatcher instance;

    public static PatternMatcher v() {
	return instance;
    }

    private PatternMatcher(PCStructure hierarchy) {
	this.hierarchy = hierarchy;

	prim_types = new HashSet();
	prim_types.add("void");
	prim_types.add("char");
	prim_types.add("byte");
	prim_types.add("short");
	prim_types.add("int");
	prim_types.add("long");
	prim_types.add("float");
	prim_types.add("double");
	prim_types.add("boolean");
    }

    public static PatternMatcher create(PCStructure hierarchy) {
	instance = new PatternMatcher(hierarchy);
	return instance;
    }

    public Pattern compileNamePattern(String name_pat) {
	if (name_pattern_cache.containsKey(name_pat)) {
	    return (Pattern)name_pattern_cache.get(name_pat);
	}
	String pat;
	// Make sure that the pattern never matches a pure integer name
	if (name_pat.equals("*")) {
	    pat = "[^0-9].*";
	} else if (name_pat.startsWith("*")) {
	    String pat_start;
	    char after_star = name_pat.charAt(1);
	    if (after_star >= '0' && after_star <= '9') {
		pat_start = "[^0-9].*";
	    } else {
		pat_start = "([^0-9].*)?";
	    }
	    pat = pat_start + name_pat.substring(1).replaceAll("\\*", ".*");
	} else {
	    char first = name_pat.charAt(0);
	    if (first >= '0' && first <= '9') {
		pat = "[a&&b]"; // The nonmatching pattern. Any better way to do it?
	    } else {
		pat = name_pat.replaceAll("\\*", ".*");
	    }
	}
	if (abc.main.Debug.v().namePatternMatches)
	    System.err.println("Compiling the name pattern component "+name_pat+" into "+pat);
	Pattern p = Pattern.compile("^"+pat+"$");
	name_pattern_cache.put(name_pat, p);
	return p;
    }

    public void computeMatches(NamePattern pat, PCNode context, Set/*<String>*/ classes, Set/*<String>*/ packages) {
	pattern_classes.put(pat, classes);
	pattern_packages.put(pat, packages);
	pattern_context.put(pat, context);
	pattern_matches.put(pat, hierarchy.matchName(pat, context, classes, packages));
    }

    public void updateWithAllSootClasses() {
	Iterator sci = Scene.v().getClasses().iterator();
	while (sci.hasNext()) {
	    SootClass sc = (SootClass)sci.next();
	    hierarchy.insertSootClass(sc, false);
	}
    }

    public void recomputeAllMatches() {
	Iterator pati = pattern_matches.keySet().iterator();
	while (pati.hasNext()) {
	    NamePattern pat = (NamePattern)pati.next();
	    PCNode context = (PCNode)pattern_context.get(pat);
	    Set classes = (Set)pattern_classes.get(pat);
	    Set packages = (Set)pattern_packages.get(pat);
	    //System.out.print("Recomputing pattern "+pat+"...");
	    pattern_matches.put(pat, hierarchy.matchName(pat, context, classes, packages));
	    //System.out.println("DONE");
	}
    }

    Set getMatches(NamePattern pat) {
	if (!pattern_matches.containsKey(pat)) {
	    throw new RuntimeException("Unknown name pattern: "+pat+" at "+pat.position());
	}
	return (Set)pattern_matches.get(pat);
    }

    public boolean matchesName(NamePattern pat, PCNode name) {
	//System.out.print("Matching pattern "+pat+"...");
	boolean res = getMatches(pat).contains(name);
	//System.out.println("DONE");
	return res;
    }

    public boolean matchesObject(NamePattern pat) {
	PCNode object = hierarchy.getClass("java.lang.Object");
	return matchesName(pat, object);
    }

    public boolean matchesClass(ClassnamePatternExpr pattern, String cl) {
	PCNode cl_node = hierarchy.getClass(cl);
	return pattern.matches(this, cl_node);
    }

    public boolean matchesType(TypePatternExpr pattern, String type) {
	//System.out.println("Matching type pattern "+pattern+" on "+pattern.position()+" to "+type+"...");
	int dim = 0;
	while (type.endsWith("[]")) {
	    dim++;
	    type = type.substring(0, type.length()-2);
	}
	if (prim_types.contains(type)) {
	    if (dim == 0) {
		return pattern.matchesPrimitive(this, type);
	    } else {
		return pattern.matchesPrimitiveArray(this, type, dim);
	    }
	} else {
	    PCNode cl_node = hierarchy.getClass(type);
	    if (dim == 0) {
		return pattern.matchesClass(this, cl_node);
	    } else {
		return pattern.matchesClassArray(this, cl_node, dim);
	    }
	}
    }

    public boolean matchesModifiers(List /*<ModifierPattern>*/ mods, soot.ClassMember thing) {
	// FIXME
	return true;
    }

    public boolean matchesFormals(List/*<FormalPattern>*/ fpats, List/*<soot.Type>*/ ftypes) {
	return matchesFormals(fpats, 0, ftypes, 0);
    }

    private boolean matchesFormals(List/*<FormalPattern>*/ fpats, int fpi, List/*<soot.Type>*/ ftypes, int fti) {
	// FIXME: BRUTE FORCE MATCHING. DO SOMETHING MORE CLEVER!
	while (fpi < fpats.size() && fti < ftypes.size()) {
	    FormalPattern fp = (FormalPattern)fpats.get(fpi);
	    soot.Type ft = (soot.Type)ftypes.get(fti);
	    if (fp instanceof TypeFormalPattern) {
		TypePatternExpr pat = ((TypeFormalPattern)fp).expr();
		if (!matchesType(pat, ft.toString())) return false;
	    } else {
		// DOTDOT
		while (fti < ftypes.size()) {
		    if (matchesFormals(fpats, fpi+1, ftypes, fti)) return true;
		    fti++;
		}
		return false;
	    }
	    fpi++;
	    fti++;
	}
	return fpi == fpats.size() && fti == ftypes.size();
    }

    public abc.weaving.aspectinfo.ClassnamePattern makeAIClassnamePattern(ClassnamePatternExpr pattern) {
	return new AIClassnamePattern(pattern);
    }

    private class AIClassnamePattern implements abc.weaving.aspectinfo.ClassnamePattern {
	ClassnamePatternExpr pattern;

	public AIClassnamePattern(ClassnamePatternExpr pattern) {
	    this.pattern = pattern;
	}

	public boolean matchesClass(SootClass sc) {
	    return PatternMatcher.this.matchesClass(pattern, sc.toString());
	}

	public String toString() {
	    return pattern.toString();
	}
    }

    public abc.weaving.aspectinfo.TypePattern makeAITypePattern(TypePatternExpr pattern) {
	return new AITypePattern(pattern);
    }

    private class AITypePattern implements abc.weaving.aspectinfo.TypePattern {
	TypePatternExpr pattern;

	public AITypePattern(TypePatternExpr pattern) {
	    this.pattern = pattern;
	}

	public boolean matchesType(Type t) {
	    return PatternMatcher.this.matchesType(pattern, t.toString());
	}

	public String toString() {
	    return pattern.toString();
	}
    }

    public abc.weaving.aspectinfo.MethodPattern makeAIMethodPattern(MethodPattern pattern) {
	if(abc.main.Debug.v.matcherWarnUnimplemented)
	    System.err.println("FIXME: Making an incomplete method pattern");
	return new AIMethodPattern(pattern);
    }

    private class AIMethodPattern implements abc.weaving.aspectinfo.MethodPattern {
	MethodPattern pattern;

	public AIMethodPattern(MethodPattern pattern) {
	    this.pattern = pattern;
	}

	public boolean matchesMethod(SootMethod method) {
	    String name = MethodCategory.getName(method);
	    String classname = MethodCategory.getClassName(method);
	    return
		matchesModifiers(pattern.getModifiers(), method) &&
		matchesType(pattern.getType(), method.getReturnType().toString()) &&
		matchesClass(pattern.getName().base(), classname) &&
		pattern.getName().name().getPattern().matcher(name).matches() &&
		matchesFormals(pattern.getFormals(), method.getParameterTypes());
	    // FIXME: need throws
	}
	public String toString() {
	    return pattern.toString();
	}
    }

    public abc.weaving.aspectinfo.FieldPattern makeAIFieldPattern(List modifiers,
								  TypePatternExpr type,
								  ClassnamePatternExpr clpat,
								  SimpleNamePattern name) {
	return new AIFieldPattern(modifiers, type, clpat, name);
    }


    private class AIFieldPattern implements abc.weaving.aspectinfo.FieldPattern {
	List/*<ModifierPattern>*/ modifiers;
	TypePatternExpr type;
	ClassnamePatternExpr clpat;
	SimpleNamePattern name;

	public AIFieldPattern(List modifiers, TypePatternExpr type, ClassnamePatternExpr clpat, SimpleNamePattern name) {
	    this.modifiers = modifiers;
	    this.type = type;
	    this.clpat = clpat;
	    this.name = name;
	}

	public boolean matchesField(SootField sf) {
	    return
		matchesModifiers(modifiers, sf) &&
		matchesType(type, sf.getType().toString()) &&
		matchesClass(clpat, sf.getDeclaringClass().toString()) &&
		name.getPattern().matcher(sf.getName()).matches();
	}

	public boolean matchesMethod(SootMethod sm) {
	    int cat = MethodCategory.getCategory(sm);
	    if (!(cat == MethodCategory.ACCESSOR_GET || cat == MethodCategory.ACCESSOR_SET)) {
		return false;
	    }
	    String name = MethodCategory.getName(sm);
	    String classname = MethodCategory.getClassName(sm);
	    //FIXME: This will not work for inner classes
	    SootField sf = Scene.v().getSootClass(classname).getField(name);
	    return matchesField(sf);
	}

	public String toString() {
	    return modifiers.toString()+" "+type.toString()+" "+clpat.toString()+" "+name.toString();
	}
    }

    public abc.weaving.aspectinfo.ConstructorPattern makeAIConstructorPattern(ConstructorPattern pattern) {
	// Assumes that name is <init>
	if(abc.main.Debug.v.matcherWarnUnimplemented)
	    System.err.println("FIXME: Making an incomplete contructor pattern");
	return new AIConstructorPattern(pattern);
    }

    private class AIConstructorPattern implements abc.weaving.aspectinfo.ConstructorPattern {
	ConstructorPattern pattern;

	public AIConstructorPattern(ConstructorPattern pattern) {
	    this.pattern = pattern;
	}

	public boolean matchesConstructor(SootMethod method) {
	    return
		matchesModifiers(pattern.getModifiers(), method) &&
		matchesClass(pattern.getName().base(), method.getDeclaringClass().toString()) &&
		matchesFormals(pattern.getFormals(), method.getParameterTypes());
	    // FIXME: need throws
	}
	public String toString() {
	    return pattern.toString();
	}
    }



}
