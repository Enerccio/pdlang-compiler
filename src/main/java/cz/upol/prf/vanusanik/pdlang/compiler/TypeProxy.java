package cz.upol.prf.vanusanik.pdlang.compiler;

public class TypeProxy {

	private String expectedPath;
	private TypeInformation type;
	
	public TypeProxy(String expectedPath) {
		this.expectedPath = expectedPath;
	}
	
	public String getExpectedPath() {
		return expectedPath;
	}
	
	public boolean isResolved() {
		return type != null;
	}
	
	public TypeInformation getType() {
		return type;
	}
	
	public void setType(TypeInformation type) {
		this.type = type;
	}

	public boolean isInvokerType() {
		return type != null && type.isInvokerType();
	}
	
}
