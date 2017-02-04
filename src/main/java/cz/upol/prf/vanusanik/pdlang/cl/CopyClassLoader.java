/**
 * PDLang
 * Copyright (c) 2016-2017 Peter Vaňušanik <admin@en-circle.com>
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package cz.upol.prf.vanusanik.pdlang.cl;

import java.util.HashMap;
import java.util.Map;

import cz.upol.prf.vanusanik.pdlang.tools.Pair;

/**
 * Implements loans from other class loaders
 * 
 * @author pvan
 */
public class CopyClassLoader extends ClassLoader {

	/**
	 * Stores loans from other class loaders
	 */
	private Map<String, Pair<String, ClassLoader>> loans = new HashMap<String, Pair<String, ClassLoader>>();

	public CopyClassLoader(ClassLoader parent) {
		super(parent);
	}

	/**
	 * Adds loaned class as sysname
	 * 
	 * @param sysname
	 * @param classDef
	 */
	protected void addLoan(String sysname, Class<?> classDef) {
		loans.put(sysname, Pair.makePair(classDef.getName(), classDef.getClassLoader()));
	}

	/**
	 * @param sysname
	 * @return if sysname is loaned or not
	 */
	protected boolean isLoan(String sysname) {
		return loans.containsKey(sysname);
	}

	/**
	 * Returns loaned class
	 * 
	 * @param sysname
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> getLoan(String sysname) throws ClassNotFoundException {
		if (!isLoan(sysname))
			throw new ClassNotFoundException("not a loan");
		return loans.get(sysname).getSecond().loadClass(loans.get(sysname).getFirst());
	}

}
