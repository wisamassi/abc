package abc.weaving.matching;

import java.util.List;
import java.util.LinkedList;

// FIXME: temporary precedence hack
import abc.weaving.aspectinfo.BeforeAdvice;

/** The list(s) of advice applying to a method
 *  @author Ganesh Sittampalam
 *  @author Laurie Hendren 
 *       (added "isEmpty and has* methods", May 4, 2004)
 *  @date 28-Apr-04
 */
public class MethodAdviceList {

    /** Advice that would apply to the whole body, i.e. at 
	execution joinpoints */
    public List/*<AdviceApplication>*/ bodyAdvice=new LinkedList();
    public void addBodyAdvice(AdviceApplication aa) {
	bodyAdvice.add(aa);
    }

    /** Advice that would apply inside the body, i.e. most other joinpoints */
    public List/*<AdviceApplication>*/ stmtAdvice=new LinkedList();
    public void addStmtAdvice(AdviceApplication aa) {
	// FIXME: temporary precedence hack
	if(aa.advice.getAdviceSpec() instanceof BeforeAdvice)
	    ((LinkedList) stmtAdvice).addFirst(aa);
	else stmtAdvice.add(aa);
    }

    /** pre-initialization joinpoints */
    public List/*<AdviceApplication>*/ preinitializationAdvice
	=new LinkedList();
    public void addPreinitializationAdvice(AdviceApplication aa) {
	preinitializationAdvice.add(aa);
    }

    /** initialization joinpoints, trigger inlining of this() calls */
    public List/*<AdviceApplication>*/ initializationAdvice=new LinkedList();
    public void addInitializationAdvice(AdviceApplication aa) {
	initializationAdvice.add(aa);
    }

    /** returns true if there is no advice */
    public boolean isEmpty() { 
        return(bodyAdvice.isEmpty() && 
	       stmtAdvice.isEmpty() &&
	       initializationAdvice.isEmpty() &&
               preinitializationAdvice.isEmpty());
    }

    /** returns true if there is any body advice */
    public boolean hasBodyAdvice() { 
      return !bodyAdvice.isEmpty();
    }

    /** returns true if there is any stmt advice */
    public boolean hasStmtAdvice() {
      return !stmtAdvice.isEmpty();
    }

    /** returns true if there is any initialization advice */
    public boolean hasInitializationAdvice() {
      return !initializationAdvice.isEmpty();
    }

    /** returns true if there is any preinitialization advice */
    public boolean hasPreinitializationAdvice() {
      return !preinitializationAdvice.isEmpty();
    }


    public String toString() {
	return "body advice: "+bodyAdvice+"\n"
	    +"statement advice: "+stmtAdvice+"\n"
	    +"preinitialization advice: "+preinitializationAdvice+"\n"
	    +"initialization advice: "+initializationAdvice+"\n";
    }
}
