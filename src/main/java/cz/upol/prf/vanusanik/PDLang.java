package cz.upol.prf.vanusanik;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.upol.prf.vanusanik.__PDLANG__.StdlibLoader;
import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;
import cz.upol.prf.vanusanik.pdlang.core.components.ModuleHolder;
import cz.upol.prf.vanusanik.pdlang.external.ExternalModuleHolder;
import cz.upol.prf.vanusanik.pdlang.external.PDLangExternalElement;
import cz.upol.prf.vanusanik.pdlang.path.InternalPDPathDescriptor;
import cz.upol.prf.vanusanik.pdlang.tools.Utils;

public class PDLang {
	
	public static PDLang getHandle() {
		return new PDLang();
	}

	private Map<String, PDLangExternalElement> bindings = new HashMap<String, PDLangExternalElement>();
	private List<PDLangLibWrapper> wrappers = Arrays.asList((PDLangLibWrapper)new StdlibLoader());
	
	private PDLangClassLoader classLoader;
	
	public void init(ClassLoader parentClassLoader) {
		classLoader = new PDLangClassLoader(parentClassLoader, this);
		for (PDLangLibWrapper wrapper : wrappers) {
			wrapper.addBindings(this);
		}
		classLoader.addBuildPath(new InternalPDPathDescriptor(StdlibLoader.class));
	}
	
	public ExternalModuleHolder getModule(String name) throws ClassNotFoundException {
		return new ModuleHolder(classLoader.loadClass(asModuleName(name)));
	}
	
	private String asModuleName(String name) {
		return "~pd"+Utils.dots2slashes(name);
	}

	public void setForeignBinding(String ppath, PDLangExternalElement e) {
		bindings.put(ppath, e);
	}

	public PDLangClassLoader getClassLoader() {
		return classLoader;
	}
}
