package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.CompilationUnitContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;

public class CompilationUnitCC implements CompilerComponent<CompilationUnitContext> {

	public Class<? extends CompilationUnitContext> getRegisterHandler() {
		return CompilationUnitContext.class;
	}

	public Object compile(CompilationUnitContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		// resolve imports
		
		// continue with compilation
		return compiler.next(syntaxElement.moduleDefinition(), compiler, state);
	}


}
