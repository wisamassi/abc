package abc.weaving.aspectinfo;

import java.util.*;
import polyglot.util.Position;
import soot.*;
import abc.weaving.matching.*;
import abc.weaving.residues.*;

/** A pattern for a single argument. */
public abstract class ArgPattern extends Syntax {
    public ArgPattern(Position pos) {
	super(pos);
    }

    /** For use when being used in an args pattern */
    public abstract Residue matchesAt(WeavingEnv we,ContextValue cv);

    /** For use when this is being used as a pointcut formal */
    public abstract Var substituteForPointcutFormal
	(Hashtable/*<String,Var>*/ renameEnv,
	 Hashtable/*<String,AbcType>*/ typeEnv,
	 Formal formal,
	 List/*<Formal>*/ newLocals,
	 List /*<CastPointcutVar>*/ newCasts,
	 Position pos);

}
