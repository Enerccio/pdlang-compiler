package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import java.util.List;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ModuleDefinitionContext;
import cz.upol.prf.vanusanik.pdlang.compiler.ClassContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;
import cz.upol.prf.vanusanik.pdlang.compiler.TypeInformation;

public class ModuleDefinitionCC implements CompilerComponent<ModuleDefinitionContext> {

	public Class<? extends ModuleDefinitionContext> getRegisterHandler() {
		return ModuleDefinitionContext.class;
	}

	@SuppressWarnings("unchecked")
	public Object compile(ModuleDefinitionContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		
		ClassContext cc = state.pushClassContext();
		String moduleName = (String) compiler.next(syntaxElement.identifier(), compiler, state);
		List<TypeInformation> initTypes = (List<TypeInformation>) compiler.next(syntaxElement.moduleInit(), compiler, state);
		
		return null;
	}

}
