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

	private Map<Character, String> type2jtype = new HashMap<Character, String>();

	public InvokerCompiler() {
		type2jtype.put('(', null);
		type2jtype.put('T', null);

		type2jtype.put('i', "I");
		type2jtype.put('f', "F");
		type2jtype.put('d', "D");
		type2jtype.put('l', "J");
		type2jtype.put('c', "C");
		type2jtype.put('b', "Z");

		type2jtype.put('I', "Ljava/lang/Integer;");
		type2jtype.put('F', "Ljava/lang/Float;");
		type2jtype.put('D', "Ljava/lang/Double;");
		type2jtype.put('L', "Ljava/lang/Long;");
		type2jtype.put('C', "Ljava/lang/Char;");
		type2jtype.put('B', "Ljava/lang/Boolean;");
		type2jtype.put('a', "Ljava/lang/Object;");
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
				if (current != '⟪')
					continue;
				String innerInvoker = "";
				int parenCount = 1;
				while (idx < invDefinition.length()) {
					char c = invDefinition.charAt(idx++);
					if (c == '⟫') {
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
