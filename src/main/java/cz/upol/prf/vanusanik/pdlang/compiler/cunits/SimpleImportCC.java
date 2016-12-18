package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.SimpleImportContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;

public class SimpleImportCC implements CompilerComponent<SimpleImportContext> {

	public Class<? extends SimpleImportContext> getRegisterHandler() {
		return SimpleImportContext.class;
	}

	public Object compile(SimpleImportContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		return null;
	}

}
