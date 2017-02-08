package cz.upol.prf.vanusanik.pdlang.compiler;

import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.CompilationException;

public class TypeInformation {

	public static enum Type {
		BASIC, FUNCTION, MODULE, CUSTOM, FOREIGN
	}

	private String javaClassName;
	private String javaTypeName;
	private String packageName;
	private String pdlangType;
	private String invokerType;
	private Type type;
	private Object carryData;

	public String getJavaClassName() {
		return javaClassName;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPdlangType() {
		return pdlangType;
	}

	public void setPdlangType(String pdlangType) {
		this.pdlangType = pdlangType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setCarryData(Object data) {
		this.carryData = data;
	}

	public Object getCarryData() {
		return this.carryData;
	}
	
	public String invokerType() {
		if (type == Type.FUNCTION) {
			return invokerType;
		} else {
			throw new CompilationException("Not an invoker type");
		}
	}
	
	public void setInvokerType(String invokerType) {
		this.invokerType = invokerType;
	}
}
