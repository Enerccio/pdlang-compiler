package cz.upol.prf.vanusanik.pdlang.path;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.upol.prf.vanusanik.pdlang.tools.Utils;

public class InternalPDPathDescriptor implements PDPathDescriptor {
	
	private FileSystemPDPathDescriptor exposed;
	
	private Class<?> placement;
	private Map<String, byte[]> loadables = new HashMap<String, byte[]>();
	private Map<String, String> names = new HashMap<String, String>();

	public InternalPDPathDescriptor(Class<?> placement) {
		this.placement = placement;
		parse();
	}

	private void parse() {
		File jarFile = new File(placement.getProtectionDomain().getCodeSource().getLocation().getPath());
		String placementPath = Utils.dots2slashes(placement.getPackage().getName());
		if (jarFile.isFile()) {
			
		} else {
			exposed = new FileSystemPDPathDescriptor(new File(jarFile, placementPath));
		}
	}

	public boolean hasPath(String path) {
		if (exposed != null) 
			return exposed.hasPath(path);
		return loadables.containsKey(path);
	}

	public byte[] getModule(String path) throws IOException {
		if (exposed != null)
			return exposed.getModule(path);
		return loadables.get(path);
	}

	public Collection<? extends String> getAvailableModules() {
		if (exposed != null)
			return exposed.getAvailableModules();
		return loadables.keySet();
	}

	public String getModuleName(String cpath) {
		if (exposed != null)
			return exposed.getModuleName(cpath);
		return names.get(cpath);
	}
}
