/* abc - The AspectBench Compiler
 * Copyright (C) 2006 Eric Bodden
 *
 * This compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this compiler, in the file LESSER-GPL;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package abc.weaving.weaver;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import polyglot.util.Enum;
import abc.main.Debug;
import abc.main.options.OptionsParser;

/**
 * Encapsulates a reweaving analysis and associates it with a unique pass ID
 * so that reweaving passes can be rescheduled properly.
 * A pass can also have a timeout after which it will be cancelled.
 * @author Eric Bodden
 */
public class ReweavingPass {

    /** Analysis identifiers. These should be unique. The ID is used to enable th eanalysis on the command line. */
    @SuppressWarnings("serial")
	public static class ID extends Enum {
        public ID(String name) { super(name);
	        if(name.contains(" ") || name.contains("-")) {
	        	throw new RuntimeException("ID must not contain whitespace or hyphens: "+name);
	        }
        }
    }

    /**
     * Timeout value stating that no timeout should be applied.
     */
    public static final long NO_TIMEOUT = 0;

    protected ID id;
    
    protected ReweavingAnalysis analysis;
    
    protected long timeout;
    
    protected volatile Throwable exception;

	protected final String name;
	
	protected final Set<ID> dependencies;

    /**
     * Creates a new reweaving analysis pass.
     * @param id a unique pass ID
     * @param analysis the analysis to schedule
     */
    public ReweavingPass(ID id, ReweavingAnalysis analysis, ID... dependencies) {
        this(id, analysis, NO_TIMEOUT, dependencies);
    }
        
    /**
     * Creates a new reweaving analysis pass.
     * @param id a unique pass ID
     * @param analysis the analysis to schedule
     * @param timeout a timeout in milliseconds after which the analysis should be aborted
     */
    public ReweavingPass(ID id, ReweavingAnalysis analysis, long timeout, ID... dependencies) {
        assert id != null;
        assert analysis != null;
        this.id = id;
        this.analysis = analysis;
        if(Debug.v().disableReweavingAnalysisTimeouts) {
            //disable timeouts
            timeout = NO_TIMEOUT;
        }
        this.timeout = timeout;
		this.name = id.toString();
		this.dependencies = Collections.unmodifiableSet(new HashSet<ID>(Arrays.asList(dependencies)));
    }

    /**
     * Returns the unique pass ID.
     * @return the id
     */
    public ID getId() {
        return id;
    }
    
    /**
     * Executes the analysis under the given timeout.
     * @return <code>true</code> if we must reweave after this analysis
     */
    public boolean analyze() {
    	
        //set up the thread
        Worker worker = new Worker();
        Thread t = new Thread(worker);
        
        UncaughtExceptionHandler uh = new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				exception = e;
			}        	
        };
		t.setUncaughtExceptionHandler(uh);
        
        //run it
        t.start();
        
        try {
            //and wait for it but at most "timeout" milliseconds
            t.join(timeout);
        } catch (InterruptedException e) {
            if(t.isInterrupted() && Debug.v().printReweavingAnalysisTimeouts) {
                System.err.println("Reweaving pass '"+id+"' timed out after "+timeout+" ms.");
            }
        }
        
        if(exception!=null) {
        	Throwable e = exception;
            exception = null;
            throw new RuntimeException("Exception while executing reweaving pass '"+id+"'",e);        	
        }

        //is false, if the worker was aborted
        return worker.result;        
    }
    
    /**
     * @see abc.weaving.weaver.ReweavingAnalysis#setupWeaving()
     */
    public void setupWeaving() {
        analysis.setupWeaving();
    }

    /**
     * @see abc.weaving.weaver.ReweavingAnalysis#tearDownWeaving()
     */
    public void tearDownWeaving() {
        analysis.tearDownWeaving();
    }

    /**
     * @see abc.weaving.weaver.ReweavingAnalysis#cleanup()
     */
    public void cleanup() {
        analysis.cleanup();
    }

    /**
     * @see abc.weaving.weaver.ReweavingAnalysis#defaultSootArgs(java.util.List)
     */
    public void defaultSootArgs(List<String> sootArgs) {
        analysis.defaultSootArgs(sootArgs);
    }

    /**
     * @see abc.weaving.weaver.ReweavingAnalysis#enforceSootArgs(java.util.List)
     */
    public void enforceSootArgs(List<String> sootArgs) {
        analysis.enforceSootArgs(sootArgs);
    }
    
    /**
     * Internal worker which allows to retrieve the result of a treaded analysis.
     * @author Eric Bodden
     */
    protected class Worker implements Runnable {

        protected boolean result = false;
        
        public void run() {
            result = analysis.analyze();
        }
        
        public boolean getResult() {
            return result;
        }
        
    }
    
    public Set<ID> dependencies() {
    	return dependencies;
    }

	public String getName() {
		return name;
	}
	
	public boolean isEnabled() {
		return Arrays.asList(OptionsParser.v().static_analyses().toLowerCase().split("-")).contains(getName());
	}
    
}
