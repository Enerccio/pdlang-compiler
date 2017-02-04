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
package cz.upol.prf.vanusanik.pdlang.compiler;

import java.io.File;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;
import cz.upol.prf.vanusanik.pdlang.path.PDPathDescriptor;

public interface PDLangCompiler {

	/**
	 * Registers path descriptor and appends it to the available paths
	 * 
	 * @param descriptor
	 */
	public void registerPDPath(PDPathDescriptor descriptor);

	/**
	 * Registers path as path descriptor. Equivalent of calling
	 * registerPDPath(new FileSystemPDPathDescriptor(systemPath))
	 * 
	 * @param systemPath
	 */
	public void registerPDPath(File systemPath);

	public <T extends ParserRuleContext> Object next(T syntaxElement, PDLangCompiler compiler, CompilerState state)
			throws Exception;

	public Class<?> compile(String className, Map<String, Class<?>> classCache, PDLangClassLoader classLoader)
			throws Exception;

	ModuleDiscoveryManager getDiscoveryManager();

}
