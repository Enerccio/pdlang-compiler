package cz.upol.prf.vanusanik.pdlang.compiler;

import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.CompilationException;

public class TypeInformation {

	public static enum Type {
		BASIC, BASIC_OBJECT, FUNCTION, STATIC_FUNCTION, MODULE, CUSTOM, FOREIGN, FOREIGN_FUNC
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
		if (isInvokerType()) {
			return invokerType;
		} else {
			throw new CompilationException("Not an invoker type");
		}
	}
	
	public void setInvokerType(String invokerType) {
		this.invokerType = invokerType;
	}

	public boolean isInvokerType() {
		return type == Type.FUNCTION || type == Type.FOREIGN_FUNC;
	}
}
