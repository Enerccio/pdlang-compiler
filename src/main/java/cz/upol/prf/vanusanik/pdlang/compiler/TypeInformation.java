package cz.upol.prf.vanusanik.pdlang.compiler;

public class TypeInformation {

	public static enum Type {
		BASIC, INVOKER, MODULE, CUSTOM
	}

	private String javaClassName;
	private String javaTypeName;
	private String packageName;
	private String pdlangType;
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
}
