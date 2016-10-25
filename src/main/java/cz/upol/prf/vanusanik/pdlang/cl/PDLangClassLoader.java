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

import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;
import cz.upol.prf.vanusanik.pdlang.compiler.InvokerCompiler;
import cz.upol.prf.vanusanik.pdlang.compiler.PDLangCompiler;
import cz.upol.prf.vanusanik.pdlang.tools.Constants;

public class PDLangClassLoader extends CopyClassLoader {
	
	private IPDLangCompiler compiler = new PDLangCompiler();
	private InvokerCompiler invCompiler = new InvokerCompiler();
	private boolean debug;
	
	public PDLangClassLoader(ClassLoader parent) {
		super(parent);
	}

	@Override
	protected Class<?> findClass(String clsName) 
			throws ClassNotFoundException {
		if (clsName.startsWith(Constants.PD_CLASSTYPE)) {
			return getPDLangObject(clsName.substring(3));
		}
		if (isLoan(clsName))
			return getLoan(clsName);
		return super.findClass(clsName);
	}

	private synchronized Class<?> getPDLangObject(String className) throws ClassNotFoundException {
		if (className.startsWith(Constants.PD_SEPARATOR + "inv")) {
			return getInvoker(className.substring(4));
		}
		return getPDLangDefinition(className);
	}
	

	private Class<?> getPDLangDefinition(String className) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Map<String, Class<?>> invokerCache = 
			new HashMap<String, Class<?>>();

	private Class<?> getInvoker(String invoker) throws ClassNotFoundException {
		if (!invokerCache.containsKey(invoker)) {
			byte[] classData = invCompiler.compileInvoker(this, invoker);
			invokerCache.put(invoker, defineClass(Constants.PD_CLASSTYPE_INVOKER + invoker, 
					classData, 0, classData.length));
		}
		return invokerCache.get(invoker);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	

}
