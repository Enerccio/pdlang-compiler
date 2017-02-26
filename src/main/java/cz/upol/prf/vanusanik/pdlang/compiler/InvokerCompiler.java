package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;
import cz.upol.prf.vanusanik.pdlang.tools.Constants;
import cz.upol.prf.vanusanik.pdlang.tools.Utils;

public class InvokerCompiler implements Opcodes {

	public static final char OBJ_TYPE_ANY = 'a';
	public static final char OBJ_TYPE_BOOL = 'B';
	public static final char OBJ_TYPE_BYTE = 'X';
	public static final char OBJ_TYPE_CHAR = 'C';
	public static final char OBJ_TYPE_LONG = 'L';
	public static final char OBJ_TYPE_DOUBLE = 'D';
	public static final char OBJ_TYPE_FLOAT = 'F';
	public static final char OBJ_TYPE_INT = 'I';
	public static final char BASIC_TYPE_BOOL = 'b';
	public static final char BASIC_TYPE_BYTE = 'x';
	public static final char BASIC_TYPE_CHAR = 'c';
	public static final char BASIC_TYPE_LONG = 'l';
	public static final char BASIC_TYPE_DOUBLE = 'd';
	public static final char BASIC_TYPE_FLOAT = 'f';
	public static final char BASIC_TYPE_INT = 'i';
	public static final char BASIC_TYPE_STR = 's';
	
	public static final char INV_SEP_RIGHT = '⟫';
	public static final char INV_SEP_LEFT = '⟪';
	
	private Map<Character, String> type2jtype = new HashMap<Character, String>();

	public InvokerCompiler() {
		type2jtype.put('(', null);
		type2jtype.put('T', null);

		type2jtype.put(BASIC_TYPE_INT, "I");
		type2jtype.put(BASIC_TYPE_FLOAT, "F");
		type2jtype.put(BASIC_TYPE_DOUBLE, "D");
		type2jtype.put(BASIC_TYPE_LONG, "J");
		type2jtype.put(BASIC_TYPE_CHAR, "C");
		type2jtype.put(BASIC_TYPE_BOOL, "Z");
		type2jtype.put(BASIC_TYPE_BYTE, "B");

		type2jtype.put(OBJ_TYPE_INT, "Ljava/lang/Integer;");
		type2jtype.put(OBJ_TYPE_FLOAT, "Ljava/lang/Float;");
		type2jtype.put(OBJ_TYPE_DOUBLE, "Ljava/lang/Double;");
		type2jtype.put(OBJ_TYPE_LONG, "Ljava/lang/Long;");
		type2jtype.put(OBJ_TYPE_CHAR, "Ljava/lang/Char;");
		type2jtype.put(OBJ_TYPE_BOOL, "Ljava/lang/Boolean;");
		type2jtype.put(OBJ_TYPE_BYTE, "Ljava/lang/Byte;");
		type2jtype.put(OBJ_TYPE_ANY, "Ljava/lang/Object;");
		
		type2jtype.put(BASIC_TYPE_STR, "Ljava/lang/String;");
	}

	/**
	 * Compiles invoker from invoker code. Invoker code is a string of types
	 * that invoker has with first type being return and other types being
	 * PDLang types
	 * 
	 * @param cl
	 * @param invDefinition
	 * @return
	 * @throws ClassNotFoundException
	 */
	public byte[] compileInvoker(PDLangClassLoader cl, String invDefinition) throws ClassNotFoundException {
		String fullClassName = Constants.PD_CLASSTYPE_INVOKER + invDefinition;
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cw.visit(V1_8, ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT, fullClassName, null, "java/lang/Object",
				new String[] {});
		int idx = 0;
		String returnType = null;
		List<String> args = new ArrayList<String>();

		while (idx < invDefinition.length()) {
			char current = invDefinition.charAt(idx++);
			String elementType = type2jtype.get(current);
			if (elementType == null) {
				if (current == 'T') {
					if (returnType == null) {
						returnType = "L" + fullClassName + ";";
					} else {
						args.add("L" + fullClassName + ";");
					}
					continue;
				}
				if (current != INV_SEP_LEFT)
					continue;
				String innerInvoker = "";
				int parenCount = 1;
				while (idx < invDefinition.length()) {
					char c = invDefinition.charAt(idx++);
					if (c == INV_SEP_RIGHT) {
						--parenCount;
						if (parenCount == 0) {
							break;
						}
					}
					innerInvoker += Character.toString(c);
				}
				Class<?> subInvoker = cl.loadClass(Constants.PD_CLASSTYPE_INVOKER + innerInvoker);
				if (returnType == null) {
					returnType = "L" + subInvoker.getName() + ";";
				} else {
					args.add("L" + subInvoker.getName() + ";");
				}
			} else {
				if (returnType == null) {
					returnType = elementType;
				} else {
					args.add(elementType);
				}
			}
		}
		cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "invoke", Utils.mkDesc(returnType, args), null,
				new String[] { "java/lang/Exception" });
		cw.visitEnd();
		byte[] classData = cw.toByteArray();
		if (cl.isDebug())
			Utils.exportToTmp(classData, fullClassName);
		return classData;
	}

}
