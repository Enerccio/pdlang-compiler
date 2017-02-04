package cz.upol.prf.vanusanik.pdlang.compiler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangBaseVisitor;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangLexer;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ForeignMethodContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ForeignTypeContext;
import cz.upol.prf.vanusanik.PDLang;
import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.CompilationException;
import cz.upol.prf.vanusanik.pdlang.external.PDExternalTypeHolder;
import cz.upol.prf.vanusanik.pdlang.external.PDLangExternalElement;
import cz.upol.prf.vanusanik.pdlang.path.PDPathDescriptor;
import cz.upol.prf.vanusanik.pdlang.tools.Utils;

public class ModuleDiscoveryManager {

	public static class ModuleDescriptor {

	}

	private Map<String, ModuleDescriptor> modules = new HashMap<String, ModuleDescriptor>();
	private List<TypeProxy> unresolvedTypes = new ArrayList<TypeProxy>();

	public void scan(final PDPathDescriptor descriptor, final PDLang context) {

		try {
			for (final String module : descriptor.getAvailableModules()) {
				final String modPath = Utils.slashes2dots(module);
				
				InputStream in = new ByteArrayInputStream(descriptor.getModule(module));

				ANTLRInputStream is = new ANTLRInputStream(in);
				pdlangLexer lexer = new pdlangLexer(is);
				lexer.removeErrorListeners();
				lexer.addErrorListener(new ThrowingErrorListener(module));
				CommonTokenStream stream = new CommonTokenStream(lexer);
				
				final ModuleDescriptor md = new ModuleDescriptor();

				pdlangBaseVisitor<Void> visitor = new pdlangBaseVisitor<Void>() {

					@Override
					public Void visitForeignType(ForeignTypeContext ctx) {
						String identifier = ctx.identifier().getText();
						String typePath = modPath + "." + identifier;
						
						PDLangExternalElement e = context.getForeignBinding(typePath);
						if (e == null || !(e instanceof PDExternalTypeHolder)) {
							throw new CompilationException("Failed to find foreign type " + typePath);
						}
						
						return super.visitForeignType(ctx);
					}

					@Override
					public Void visitForeignMethod(ForeignMethodContext ctx) {

						return super.visitForeignMethod(ctx);
					}

				};

				pdlangParser parser = new pdlangParser(stream);
				parser.removeErrorListeners();
				parser.addErrorListener(new ThrowingErrorListener(module));

				visitor.visit(parser.compilationUnit());
			}
			
			for (TypeProxy proxy : unresolvedTypes) {
				// TODO: resolve proxies
			}
		} catch (Exception e) {
			throw new CompilationException(e);
		}

	}

}
