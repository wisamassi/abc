package arc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import polyglot.ext.jl.ast.Node_c;

public class FormalPattern_c extends Node_c 
                             implements FormalPattern
{

    public FormalPattern_c(Position pos) {
        super(pos);
    }

}
