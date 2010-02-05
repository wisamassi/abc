package tests;

import junit.framework.TestCase;
import AST.Access;
import AST.Expr;
import AST.ExprStmt;
import AST.LabeledStmt;
import AST.MethodAccess;
import AST.Program;
import AST.RawCU;
import AST.RefactoringException;
import AST.Stmt;

public class InlineMethodTests extends TestCase {
	public InlineMethodTests(String name) {
		super(name);
	}
	
	private MethodAccess findAccess(Program in) {
		Expr e = in.findDoublyParenthesised();
		if(e != null)
			e.unparenthesise();
		if(e != null && e.isMethodAccess())
			return (MethodAccess)((Access)e).lastAccess();
		LabeledStmt l = in.findStmtWithLabel("inline");
		assertTrue("not found", l != null);
		Stmt s = l.unlabel();
		assertTrue("not found", s instanceof ExprStmt && ((ExprStmt)s).getExpr().isMethodAccess());
		return (MethodAccess)((Access)((ExprStmt)s).getExpr()).lastAccess();
	}
	
	public void testSucc(Program in, Program out) {		
		assertNotNull(in);
		assertNotNull(out);
		MethodAccess m = findAccess(in);
		try {
			m.doInline();
			assertEquals(out.toString(), in.toString());
		} catch(RefactoringException rfe) {
			fail(rfe.getMessage());
		}
	}

	public void testFail(Program in) {		
		assertNotNull(in);
		MethodAccess m = findAccess(in);
		try {
			m.doInline();
			assertEquals("<failed>", in.toString());
		} catch(RefactoringException rfe) { }
	}

    public void test1() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        inline: n();" +
            "        System.out.println(\"world!\");" +
            "    }" +
            "    void n() {" +
            "        System.out.println(\"Hello, \");" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    System.out.println(\"Hello, \");" +
            "    System.out.println(\"world!\");" +
            "  }" +
            "  void n() {" +
            "    System.out.println(\"Hello, \");" +
            "  }" +
            "}")));
    }

    public void test2() {
        testFail(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        inline: n();" +
            "        System.out.println(\"world!\");" +
            "    }" +
            "    void n() {" +
            "        System.out.println(\"Hello, \");" +
            "    }" +
            "}" +
            "" +
            "class B extends A {" +
            "    void n() {" +
            "        System.out.println(\"Howdy, \");" +
            "    }" +
            "}")));
    }

    public void test3() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        String msg = \"Hello, \";" +
            "        inline: n(msg);" +
            "        System.out.println(\"world!\");" +
            "    }" +
            "    void n(String msg) {" +
            "        System.out.println(msg);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    String msg = \"Hello, \";" +
            "    String msg0 = msg;" +
            "    System.out.println(msg0);" +
            "    System.out.println(\"world!\");" +
            "  }" +
            "  void n(String msg) {" +
            "    System.out.println(msg);" +
            "  }" +
            "}")));
    }

    public void test4() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i;" +
            "        i = ((n(23)));" +
            "    }" +
            "    int n(int i) {" +
            "        return 42;" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i;" +
            "    int i0 = 23;" +
            "    i = 42;" +
            "  }" +
            "  int n(int i) {" +
            "    return 42;" +
            "  }" +
            "}")));
    }

    public void test5() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        inline: n(i++);" +
            "    }" +
            "    void n(int j) {" +
            "        if(j == 23)" +
            "            System.out.println(\"magic number!\");" +
            "        else" +
            "            System.out.println(\"something else\");" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    int j = i++;" +
            "    if(j == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else\");" +
            "  }" +
            "  void n(int j) {" +
            "    if(j == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else\");" +
            "  }" +
            "}")));
    }

    public void test6() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        inline: n(i++);" +
            "    }" +
            "    void n(int j) {" +
            "        if(j == 23)" +
            "            System.out.println(\"magic number!\");" +
            "        else" +
            "            System.out.println(\"something else: \"+j);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    int j = i++;" +
            "    if(j == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else: \" + j);" +
            "  }" +
            "  void n(int j) {" +
            "    if(j == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else: \" + j);" +
            "  }" +
            "}")));
    }

    public void test7() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        inline: n(i++);" +
            "    }" +
            "    void n(int i) {" +
            "        if(i == 23)" +
            "            System.out.println(\"magic number!\");" +
            "        else" +
            "            System.out.println(\"something else\");" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    int i0 = i++;" +
            "    if(i0 == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else\");" +
            "  }" +
            "  void n(int i) {" +
            "    if(i == 23) " +
            "      System.out.println(\"magic number!\");" +
            "    else " +
            "      System.out.println(\"something else\");" +
            "  }" +
            "}")));
    }

    public void test8() {
        testFail(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        class String { }" +
            "        class java { }" +
            "        inline: n();" +
            "    }" +
            "    void n() {" +
            "        String msg = \"Hello, world!\";" +
            "        System.out.println(msg);" +
            "    }" +
            "}")));
    }

    public void test9() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int x = 23;" +
            "        inline: n();" +
            "    }" +
            "    int x = 42;" +
            "    void n() {" +
            "        System.out.println(x);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int x = 23;" +
            "    System.out.println(this.x);" +
            "  }" +
            "  int x = 42;" +
            "  void n() {" +
            "    System.out.println(x);" +
            "  }" +
            "}")));
    }

    public void test10() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        inline: n(23);" +
            "        System.out.println(\"back\");" +
            "    }" +
            "    void n(int i) {" +
            "        if(i == 42)" +
            "            return;" +
            "        System.out.println(\"here; i == \"+i);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    l:{" +
            "      int i0 = 23;" +
            "      if(i0 == 42) " +
            "        break l;" +
            "      System.out.println(\"here; i == \" + i0);" +
            "    }" +
            "    System.out.println(\"back\");" +
            "  }" +
            "  void n(int i) {" +
            "    if(i == 42) " +
            "      return ;" +
            "    System.out.println(\"here; i == \" + i);" +
            "  }" +
            "}")));
    }

    public void test11() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int j;" +
            "        j = ((n(23)));" +
            "        System.out.println(\"back\");" +
            "    }" +
            "    int n(int i) {" +
            "        System.out.println(\"here\");" +
            "        return i = 42;" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int j;" +
            "    int i = 23;" +
            "    System.out.println(\"here\");" +
            "    j = i = 42;" +
            "    System.out.println(\"back\");" +
            "  }" +
            "  int n(int i) {" +
            "    System.out.println(\"here\");" +
            "    return i = 42;" +
            "  }" +
            "}")));
    }

    public void test12() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int j;" +
            "        int i = 23;" +
            "        j = ((n(i)));" +
            "        System.out.println(\"back\");" +
            "    }" +
            "    int n(int i) {" +
            "        System.out.println(\"here\");" +
            "        return i = 42;" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int j;" +
            "    int i = 23;" +
            "    int i0 = i;" +
            "    System.out.println(\"here\");" +
            "    j = i0 = 42;" +
            "    System.out.println(\"back\");" +
            "  }" +
            "  int n(int i) {" +
            "    System.out.println(\"here\");" +
            "    return i = 42;" +
            "  }" +
            "}")));
    }

    public void test13() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i;" +
            "        inline: n();" +
            "    }" +
            "    void n() {" +
            "        int i;" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i;" +
            "    int i0;" +
            "  }" +
            "  void n() {" +
            "    int i;" +
            "  }" +
            "}")));
    }

    public void test14() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        l : {" +
            "            inline: n(23);" +
            "        }" +
            "        System.out.println(\"back\");" +
            "    }" +
            "    void n(int i) {" +
            "        if(i == 42)" +
            "            return;" +
            "        System.out.println(\"here; i == \"+i);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    l:{" +
            "      l0:{" +
            "        int i0 = 23;" +
            "        if(i0 == 42) " +
            "          break l0;" +
            "        System.out.println(\"here; i == \" + i0);" +
            "      }" +
            "    }" +
            "    System.out.println(\"back\");" +
            "  }" +
            "  void n(int i) {" +
            "    if(i == 42) " +
            "      return ;" +
            "    System.out.println(\"here; i == \" + i);" +
            "  }" +
            "}")));
    }

    public void test15() {
        testSucc(
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "    void m() {" +
            "        int i = 23;" +
            "        inline: n(23);" +
            "        System.out.println(\"back\");" +
            "    }" +
            "    void n(int i) {" +
            "        l : while(i == 2) { }" +
            "        if(i == 42)" +
            "            return;" +
            "        System.out.println(\"here; i == \"+i);" +
            "    }" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "class A {" +
            "  void m() {" +
            "    int i = 23;" +
            "    l0:{" +
            "      int i0 = 23;" +
            "      l:" +
            "        while(i0 == 2){" +
            "        }" +
            "      if(i0 == 42) " +
            "        break l0;" +
            "      System.out.println(\"here; i == \" + i0);" +
            "    }" +
            "    System.out.println(\"back\");" +
            "  }" +
            "  void n(int i) {" +
            "    l:" +
            "      while(i == 2){" +
            "      }" +
            "    if(i == 42) " +
            "      return ;" +
            "    System.out.println(\"here; i == \" + i);" +
            "  }" +
            "}")));
    }

    public void test16() {
        testFail(Program.fromStmts("inline: Integer.parseInt(\"42\");"));
    }
}