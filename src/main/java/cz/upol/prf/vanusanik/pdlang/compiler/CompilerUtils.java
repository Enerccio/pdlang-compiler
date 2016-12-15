package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CompilerUtils {

	public static String removeLastSlashedElement(String className) {
		String[] elements = className.split(Pattern.quote("/"));
		ArrayList<String> els = new ArrayList<String>(Arrays.asList(elements));
		els.remove(els.size()-1);
		return StringUtils.join(els, "/");
	}

	public static String removeColon(String className) {
		if (!className.contains(":"))
			return className;
		return className.substring(0, className.lastIndexOf(":"));
	}

}
