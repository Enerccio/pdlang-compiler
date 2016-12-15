package cz.upol.prf.vanusanik.pdlang.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

public interface CompilerComponent<T extends ParserRuleContext> {

	public Class<? extends T> getRegisterHandler();
	
	public Object compile(T syntaxElement, IPDLangCompiler compiler, 
			CompilerState state) throws Exception;
	
}
