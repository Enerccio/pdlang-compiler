package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.CompilationUnitContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.PDLangCompiler;

public class CompilationUnitCC implements CompilerComponent<CompilationUnitContext> {

	public Class<? extends CompilationUnitContext> getRegisterHandler() {
		return CompilationUnitContext.class;
	}

	public Object compile(CompilationUnitContext syntaxElement, PDLangCompiler compiler, CompilerState state)
			throws Exception {
		state.pushResolveContext();
		state.pushClassContext();
		try {
			// resolve imports
			compiler.next(syntaxElement.imports(), compiler, state);
			// continue with compilation
			return compiler.next(syntaxElement.moduleDefinition(), compiler, state);
		} finally {
			state.popResolveContext();
			state.popClassContext();
		}
	}

}
