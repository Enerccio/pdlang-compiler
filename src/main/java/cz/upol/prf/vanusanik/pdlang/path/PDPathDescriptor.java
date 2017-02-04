package cz.upol.prf.vanusanik.pdlang.path;

import java.io.IOException;
import java.util.Collection;

public interface PDPathDescriptor {

	public boolean hasPath(String path);

	public byte[] getModule(String path) throws IOException;

	public Collection<? extends String> getAvailableModules();

	public String getModuleName(String cpath);

}
