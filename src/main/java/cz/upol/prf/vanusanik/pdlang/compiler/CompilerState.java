package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import cz.upol.prf.vanusanik.pdlang.tools.Pair;

public class CompilerState {

	private LinkedList<ParserRuleContext> executionStackTree = new LinkedList<ParserRuleContext>();

	public void pushTree(ParserRuleContext syntaxElement) {
		executionStackTree.push(syntaxElement);
	}

	public void popTree(ParserRuleContext syntaxElement) {
		executionStackTree.pop();
	}

	private LinkedList<ClassContext> classContext = new LinkedList<ClassContext>();

	public ClassContext pushClassContext() {
		classContext.add(new ClassContext());
		return classContext.getLast();
	}

	public ClassContext getClassContext() {
		return getClassContext(0);
	}

	public ClassContext getClassContext(int n) {
		if (n > classContext.size())
			return null;
		return classContext.get(classContext.size() - (1 + n));
	}

	public void popClassContext() {
		classContext.pop();
	}

	private HashMap<String, TypeInformation> typeMap = new HashMap<String, TypeInformation>();

	private LinkedList<ResolveContext> resolveContext = new LinkedList<ResolveContext>();

	public void pushResolveContext() {
		resolveContext.add(new ResolveContext());
	}

	public void popResolveContext() {
		resolveContext.pop();
	}

	public Pair<Integer, TypeInformation> resolveType(String identifier) {
		List<ResolveContext> reverse = new ArrayList<ResolveContext>(resolveContext);
		int level = 0;
		for (ResolveContext rc : reverse) {
			if (rc.hasIdentifier(identifier)) {
				return Pair.makePair(level, rc.resolveType(identifier));
			}
			++level;
		}
		return null;
	}

	public void addType(TypeInformation type, String identifier) {
		resolveContext.peekLast().addType(type, identifier);
	}

	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public HashMap<String, TypeInformation> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(HashMap<String, TypeInformation> typeMap) {
		this.typeMap = typeMap;
	}

	private String packageName;

	public String getPackage() {
		return packageName;
	}

	public void setPackage(String packageName) {
		this.packageName = packageName;
	}
}
