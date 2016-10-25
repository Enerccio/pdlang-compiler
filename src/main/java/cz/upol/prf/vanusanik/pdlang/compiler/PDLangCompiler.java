package cz.upol.prf.vanusanik.pdlang.compiler;

import java.io.File;

import cz.upol.prf.vanusanik.pdlang.path.FileSystemPDPathDescriptor;
import cz.upol.prf.vanusanik.pdlang.path.PDPathDescriptor;

public class PDLangCompiler implements IPDLangCompiler {

	public void registerPDPath(PDPathDescriptor descriptor) {
		// TODO Auto-generated method stub

	}

	public void registerPDPath(File systemPath) {
		registerPDPath(new FileSystemPDPathDescriptor(systemPath));
	}

}
