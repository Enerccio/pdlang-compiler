package cz.upol.prf.vanusanik.pdlang.path;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileSystemPDPathDescriptor implements PDPathDescriptor {
	
	private List<File> plangFiles;
	private File path;

	public FileSystemPDPathDescriptor(File systemPath) {
		plangFiles = getPDLangFiles(systemPath);
		this.path = systemPath;
	}

	private List<File> getPDLangFiles(File path) {
		List<File> files = new ArrayList<File>();
		for (File entry : path.listFiles()) {
			if (entry.isDirectory()) {
				files.addAll(getPDLangFiles(entry));
			} else {
				if (entry.getName().endsWith(".pdl"))
					files.add(entry);
			}
		}
		return files;
	}

	public boolean hasPath(String path) {
		return asModuleFile(path).exists() && !asModuleFile(path).isDirectory();
	}

	public byte[] getModule(String path) throws IOException {
		File moduleFile = asModuleFile(path);
		return IOUtils.toByteArray(new FileInputStream(moduleFile));
	}

	private File asModuleFile(String path) {
		if (File.separatorChar != '/') {
			path = path.replace('/', File.separatorChar);
		}
		path += ".pdl";
		return new File(this.path, path);
	}

	public Collection<? extends String> getAvailableModules() {
		List<String> modules = new ArrayList<String>();
		for (File f : plangFiles) {
			String bp = f.getAbsolutePath().replace(path.getAbsolutePath(), "");
			if (bp.startsWith(File.separator)) {
				bp = bp.substring(1);
			}
			bp = bp.substring(0, bp.lastIndexOf("."));
			bp = bp.replace(File.separatorChar, '/');
			modules.add(bp);
		}
		return modules;
	}

}
