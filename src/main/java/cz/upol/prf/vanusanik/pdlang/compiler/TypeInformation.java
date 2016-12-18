package cz.upol.prf.vanusanik.pdlang.compiler;

public class TypeInformation {
	
	private String javaClassName;
	private String javaTypeName;
	private String packageName;
	private String pdlangType;
	
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
}
