package cz.upol.prf.vanusanik.pdlang.core.components.exceptions;

public class SystemException extends RuntimeException {
	private static final long serialVersionUID = 8575413724889993403L;

	public SystemException(Throwable t) {
		super(t);
	}
	
	public SystemException(String message) {
		super(message);
	}
}
