package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.IdentifierContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;

public class IdentifierCC implements CompilerComponent<IdentifierContext> {

	public Class<? extends IdentifierContext> getRegisterHandler() {
		return IdentifierContext.class;
	}

	public Object compile(IdentifierContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		return syntaxElement.getText();
	}

}
