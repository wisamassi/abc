/**
 * Test the value of 'count' is 1 in presence of inheritance.
 * Here we have to decide what is the most specific jpi.  As CLOS
 * the default behavior is that the most specific advice should be
 * JP2.
 */

/***
 * I think that the best solution to the most specific jpi, probably the laziest one, is that the 
 * advice order declaration determines what advice goes first.
 */

import org.aspectj.testing.Tester;

jpi void JP(int amount);
jpi void JP2(int items, InheritanceMatch3 s) extends JP(items); //by default this jpi is the most specific
jpi void JP3(int x) extends JP(x);

public class InheritanceMatch3{

    public static int count = 0;

    exhibits void JP(int i):
        execution(* foo(..)) && args(i);

    exhibits void JP2(int z, InheritanceMatch3 i ):
        execution(void foo(int)) && args(z) && this(i);

    exhibits void JP3(int i):
        execution(* foo(..)) && args(i);


    void foo(int x){}

    public static void main(String[] args){
        new InheritanceMatch3().foo(6);
    }
}

aspect A{

    before JP(int a){
    	InheritanceMatch3.count++;
    }

    before JP3(int a){
    	InheritanceMatch3.count++;
        Tester.checkEqual(InheritanceMatch3.count,1,"expected 1 but saw"+InheritanceMatch3.count);    	    	
    }

    before JP2(int a, InheritanceMatch3 im){
    	InheritanceMatch3.count++;
        Tester.checkEqual(InheritanceMatch3.count,2,"expected 2 but saw"+InheritanceMatch3.count);    	
    }    
}