package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ImportsContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.SimpleImportContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.PDLangCompiler;

public class ImportsCC implements CompilerComponent<ImportsContext> {

	public Class<? extends ImportsContext> getRegisterHandler() {
		return ImportsContext.class;
	}

	public Object compile(ImportsContext syntaxElement, PDLangCompiler compiler, CompilerState state)
			throws Exception {
		for (SimpleImportContext si : syntaxElement.simpleImport()) {
			compiler.next(si, compiler, state);
		}
		return null;
	}

}
