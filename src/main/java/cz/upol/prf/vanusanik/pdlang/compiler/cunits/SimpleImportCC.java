package cz.upol.prf.vanusanik.pdlang.compiler.cunits;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.SimpleImportContext;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerComponent;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerState;
import cz.upol.prf.vanusanik.pdlang.compiler.CompilerUtils;
import cz.upol.prf.vanusanik.pdlang.compiler.IPDLangCompiler;
import cz.upol.prf.vanusanik.pdlang.compiler.TypeInformation;
import cz.upol.prf.vanusanik.pdlang.compiler.TypeInformation.Type;

public class SimpleImportCC implements CompilerComponent<SimpleImportContext> {

	public Class<? extends SimpleImportContext> getRegisterHandler() {
		return SimpleImportContext.class;
	}

	public Object compile(SimpleImportContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		
		String moduleName = (String)compiler.next(syntaxElement.moduleName(), compiler, state);
		String javaClassName = CompilerUtils.asJavaName(moduleName);
		
		TypeInformation ti = new TypeInformation();
		
		ti.setJavaClassName(javaClassName);
		ti.setPdlangType(moduleName);
		ti.setPackageName(CompilerUtils.removeLastDot(moduleName));
		ti.setJavaTypeName("L" + javaClassName + ";");
		ti.setType(Type.MODULE);
		ti.setCarryData(syntaxElement.moduleInitExpression());
		
		state.getTypeMap().put(CompilerUtils.moduleName(moduleName), ti);
		
		return ti;
	}

}
