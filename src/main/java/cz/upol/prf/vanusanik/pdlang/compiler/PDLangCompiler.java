package cz.upol.prf.vanusanik.pdlang.compiler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangLexer;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser;
import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.CompilationUnitCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.FqNameCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.IdentifierCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.ImportsCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.ModuleDefinitionCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.ModuleNameCC;
import cz.upol.prf.vanusanik.pdlang.compiler.cunits.SimpleImportCC;
import cz.upol.prf.vanusanik.pdlang.path.FileSystemPDPathDescriptor;
import cz.upol.prf.vanusanik.pdlang.path.PDPathDescriptor;

public class PDLangCompiler implements IPDLangCompiler {
	
	private List<PDPathDescriptor> descriptors = new ArrayList<PDPathDescriptor>();
	private Map<Class<?>, CompilerComponent<? extends ParserRuleContext>> commands
		= new HashMap<Class<?>, CompilerComponent<? extends ParserRuleContext>>();
	
	public PDLangCompiler() {
		init();
	}
	
	protected void init() {
		addCompilerUnit(new CompilationUnitCC());
		addCompilerUnit(new ImportsCC());
		addCompilerUnit(new SimpleImportCC());
		addCompilerUnit(new ModuleNameCC());
		addCompilerUnit(new FqNameCC());
		addCompilerUnit(new ModuleDefinitionCC());
		
		addCompilerUnit(new IdentifierCC());
	}

	private void addCompilerUnit(CompilerComponent<? extends ParserRuleContext> cc) {
		commands.put(cc.getRegisterHandler(), cc);
	}

	public void registerPDPath(PDPathDescriptor descriptor) {
		descriptors.add(descriptor);
	}

	public void registerPDPath(File systemPath) {
		registerPDPath(new FileSystemPDPathDescriptor(systemPath));
	}

	@SuppressWarnings("unchecked")
	public Object next(ParserRuleContext syntaxElement, IPDLangCompiler compiler, CompilerState state)
			throws Exception {
		state.pushTree(syntaxElement);
		try {
			if (commands.containsKey(syntaxElement.getClass())) {
				return ((CompilerComponent<ParserRuleContext>)
						commands.get(syntaxElement.getClass())).compile(syntaxElement, compiler, state);
			}
			throw new RuntimeException("Missing compiler component for " + syntaxElement.getClass().getSimpleName());
		} finally {
			state.popTree(syntaxElement);
		}
	}

	public Class<?> compile(String className, Map<String, Class<?>> classCache, PDLangClassLoader classLoader) 
			throws Exception {
		InputStream in = findPhysicalData(className);
		
		ANTLRInputStream is = new ANTLRInputStream(in);
		pdlangLexer lexer = new pdlangLexer(is);
		lexer.removeErrorListeners();
		lexer.addErrorListener(new ThrowingErrorListener(className));
		CommonTokenStream stream = new CommonTokenStream(lexer);
		pdlangParser parser = new pdlangParser(stream);
		parser.removeErrorListeners();
		parser.addErrorListener(new ThrowingErrorListener(className));
		
		CompilerState state = new CompilerState();
		state.setSource(findPhysicalDataName(className));
		next(parser.compilationUnit(), this, state);
		
		return null;
	}

	private String findPhysicalDataName(String className) {
		String cpath = CompilerUtils.removeColon(className);
		for (PDPathDescriptor pathDesc : descriptors) {
			if (pathDesc.hasPath(cpath)) {
				return pathDesc.getModuleName(cpath);
			}
		}
		
		return null;
	}

	private InputStream findPhysicalData(String className) throws IOException {
		String cpath = CompilerUtils.removeColon(className);
		for (PDPathDescriptor pathDesc : descriptors) {
			if (pathDesc.hasPath(cpath)) {
				return new ByteArrayInputStream(pathDesc.getModule(cpath));
			}
		}
		
		return null;
	}

}
