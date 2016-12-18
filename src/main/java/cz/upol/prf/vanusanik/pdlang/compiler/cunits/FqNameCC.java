package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.FqNameContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;

public class FqNameCC implements CompilerComponent<FqNameContext> {

	public Class<? extends FqNameContext> getRegisterHandler() {
		return FqNameContext.class;
	}

	public Object compile(FqNameContext syntaxElement, IPDLangCompiler compiler, CompilerState state) throws Exception {
		return syntaxElement.getText();
	}

}
