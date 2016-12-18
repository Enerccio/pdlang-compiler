package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ModuleNameContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;

public class ModuleNameCC implements CompilerComponent<ModuleNameContext> {

	public Class<? extends ModuleNameContext> getRegisterHandler() {
		return ModuleNameContext.class;
	}

	public Object compile(ModuleNameContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		return compiler.next(syntaxElement.fqName(), compiler, state);
	}

}
