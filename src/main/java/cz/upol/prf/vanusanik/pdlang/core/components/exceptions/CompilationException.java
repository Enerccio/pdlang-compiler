package cz.upol.prf.vanusanik.pdlang.core.components.exceptions;

public class CompilationException extends RuntimeException {
	private static final long serialVersionUID = 1721657336504439196L;

	public CompilationException(Throwable t) {
		super(t);
	}

	public CompilationException(String message) {
		super(message);
	}
}
