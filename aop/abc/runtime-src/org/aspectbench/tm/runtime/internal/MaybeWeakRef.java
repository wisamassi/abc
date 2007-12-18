package org.aspectbench.tm.runtime.internal;

/* abc - The AspectBench Compiler
 * Copyright (C) 2007 Pavel Avgustinov
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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * Weak ref implementation that allows the reference to be strengthened if necessary.
 * Equality is reference identity of referents. Hashcode is identityHashCode of referent,
 * even after referent expires.
 * 
 * Weak references are canonicalised to guarantee they all expire at the same time and only
 * appear in the reference queue (if any) once.
 * 
 * @author Pavel Avgustinov
 */
public class MaybeWeakRef extends MyWeakRef {
	private static WeakKeyCollectingIdentityHashMap refMap = new WeakKeyCollectingIdentityHashMap();
	private static ReferenceQueue expiredQueue = new ReferenceQueue();
	private Object referent = null;
	
	public synchronized static MyWeakRef getWeakRef(Object o) {
		if(o instanceof MaybeWeakRef) return (MyWeakRef)o;
		//if(o == null) throw new RuntimeException("Getting weak reference for null");
		MyWeakRef ref = (MyWeakRef)refMap.get(o);
		if(ref == null) {
			ref = new MaybeWeakRef(o, expiredQueue);
			refMap.put(o, ref);
		}
		return ref;
	}

	public synchronized static void checkExpired() {
		Reference expired = expiredQueue.poll();
		while (expired != null) {
			if (expired != null)
				((MaybeWeakRef) expired).notifyContainers();
			expired = expiredQueue.poll();
		}
	}

	protected MaybeWeakRef(Object ref) {
		super(ref);
	}
	
	protected MaybeWeakRef(Object ref, ReferenceQueue q) {
		super(ref, q, true);
	}
	
	/**
	 * Only one PersistentWeakRef is ever constructed for a particular object. Thus,
	 * this PWR is equal to obj iff they are the same weak reference, or if obj is
	 * the referent of this.
	 */
	public boolean equals(Object obj) {
		return this == obj || this.get() == obj;
	}

	/**
	 * Strengthen a weak ref by keeping a strong ref in addition. Referent must be
	 * passed as an argument to avoid race conditions with the GC.
	 */
	public void strengthen(Object obj) {
		//if(obj != get()) throw new RuntimeException("Attempting to strengthen weakref to incorrect object");
		referent = obj;
	}
	
	/**
	 * Weaken the reference by dropping the strong link to the referent.
	 */
	public void weaken() {
		referent = null;
	}
}
