/* abc - The AspectBench Compiler
 * Copyright (C) 2007 Eric Bodden
 * Copyright (C) 2007 Reehan Shaikh
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
package abc.ra.ast;

import polyglot.types.TypeSystem;
import abc.aspectj.ast.AdviceDecl;
import abc.tm.ast.TMDecl;

/**
 * A relational advice declaration.
 * @author Eric Bodden
 */
public interface RelAdviceDecl extends AdviceDecl {
	
	/**
	 * Generates a tracematch declaration equivalent to this relational aspect declaration.
	 * @param container the relational aspect containing this advice
	 * @param nf the node factory
	 * @param ts the type system
	 * @return the equivalent tracematch declaration
	 */
	public TMDecl genTraceMatch(RelAspectDecl container, RANodeFactory nf, TypeSystem ts);

}
