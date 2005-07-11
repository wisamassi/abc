/* abc - The AspectBench Compiler
 * Copyright (C) 2004 Julian Tibble
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

package abc.tm.ast;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;

import polyglot.ext.jl.ast.*;

import abc.aspectj.ast.*;

import java.util.*;

/**
 * @author Julian Tibble
 */
public class SymbolDecl_c extends Node_c implements SymbolDecl
{
    private String name;
    private SymbolKind kind;
    private Pointcut pc;

    public SymbolDecl_c(Position pos, String name,
                            SymbolKind kind, Pointcut pc)
    {
        super(pos);
        this.name = name;
        this.kind = kind;
        this.pc = pc;
    }

    public String name()
    {
        return name;
    }

    public Pointcut getPointcut()
    {
        return pc;
    }

    public String kind()
    {
        return kind.kind();
    }

    // 
    // visitor handling code
    //
    protected Node reconstruct(Node n, SymbolKind kind, Pointcut pc)
    {
        if (kind != this.kind || pc != this.pc)
        {
            SymbolDecl_c new_n = (SymbolDecl_c) n.copy();
            new_n.kind = kind;
            new_n.pc = pc;

            return new_n;
        }
        return n;
    }

    public Node visitChildren(NodeVisitor v)
    {
        Node n = super.visitChildren(v);

        SymbolKind kind = (SymbolKind) visitChild(this.kind, v);
        Pointcut pc = (Pointcut) visitChild(this.pc, v);

        return reconstruct(n, kind, pc);
    }



    public AdviceDecl generateAdviceDecl(TMNodeFactory nf, List formals,
                                            TypeNode voidn, String tm_id,
                                            Position tm_pos)
    {
        // Generate AdviceSpec
        AdviceSpec spec = kind.generateAdviceSpec(nf, formals, voidn);

        // Generate an empty `throws' list
        List tlist = new LinkedList();

        // Generate the TMAdviceDecl
        return nf.TMAdviceDecl(position(), Flags.NONE, spec, tlist,
                                pc, body(nf), tm_id, tm_pos, false);
    }

    /**
     * Create an empty advice body.
     *
     * FIXME: Just for debugging, the advice prints a message
     *        instead of being empty.
     */
    private Block body(TMNodeFactory nf)
    {
        Position cg = Position.COMPILER_GENERATED;

        AmbReceiver sysout =
                nf.AmbReceiver(cg, nf.AmbPrefix(cg, null, "System"), "out");

        List args = new LinkedList();
        args.add(nf.StringLit(cg, name));

        Call println_call = nf.Call(cg, sysout, "println", args);

        Stmt println_statement = nf.Eval(cg, println_call);

        List statements = new LinkedList();
        statements.add(println_statement);

        return nf.Block(cg, statements);
    }

    public Collection binds()
    {
        return pc.mustBind();
    }
}
