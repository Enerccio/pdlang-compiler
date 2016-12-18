package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.HashMap;
import java.util.Map;

public class ResolveContext {
	private Map<String, TypeInformation> scopedTypes
		= new HashMap<String, TypeInformation>();
	
	public TypeInformation resolveType(String identifier) {
		return scopedTypes.get(identifier);
	}

	public void addType(TypeInformation type, String identifier) {
		scopedTypes.put(identifier, type);
	}

	public boolean hasIdentifier(String identifier) {
		return scopedTypes.containsKey(identifier);
	}

}
