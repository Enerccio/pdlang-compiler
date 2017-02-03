package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import java.util.List;

import org.objectweb.asm.Opcodes;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ModuleDefinitionContext;
import cz.upol.prf.vanusanik.pdlang.compiler.ClassContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerUtils;
import cz.upol.prf.vanusanik.pdlang.compiler.PDLangCompiler;
import cz.upol.prf.vanusanik.pdlang.compiler.TypeInformation;

public class ModuleDefinitionCC implements CompilerComponent<ModuleDefinitionContext>, Opcodes {

	public Class<? extends ModuleDefinitionContext> getRegisterHandler() {
		return ModuleDefinitionContext.class;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Object compile(ModuleDefinitionContext syntaxElement, PDLangCompiler compiler, CompilerState state)
			throws Exception {
		
		ClassContext ctx = state.getClassContext();
		String moduleName = (String) compiler.next(syntaxElement.identifier(), compiler, state);
		
		ctx.cw().visit(V1_8, ACC_PUBLIC + ACC_SUPER, CompilerUtils.asJavaName(state.getPackage() + moduleName), null,
				"java/lang/Object", null);
		ctx.cw().visitSource(state.getSource(), null);
		
		List<TypeInformation> initTypes = (List<TypeInformation>) compiler.next(syntaxElement.moduleInit(), compiler, state);
		
		return null;
	}

}
