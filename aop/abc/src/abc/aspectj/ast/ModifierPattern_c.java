package arc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import polyglot.ext.jl.ast.Node_c;

public class ModifierPattern_c extends Node_c implements ModifierPattern
{
    protected Flags modifier;
    protected boolean positive;

    public ModifierPattern_c(Position pos, 
			     Flags modifier, 
			     boolean positive)  {
	super(pos);
        this.modifier = modifier;
	this.positive = positive;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	if (!positive)
	    w.write("!");
        w.write(modifier.translate());
    }

}
