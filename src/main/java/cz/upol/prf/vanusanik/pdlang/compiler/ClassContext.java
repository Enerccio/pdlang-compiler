package cz.upol.prf.vanusanik.pdlang.compiler;

import org.objectweb.asm.ClassWriter;

public class ClassContext {

	private ClassWriter cw;

	public ClassContext() {
		cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
	}

	public ClassWriter cw() {
		return cw;
	}

}
