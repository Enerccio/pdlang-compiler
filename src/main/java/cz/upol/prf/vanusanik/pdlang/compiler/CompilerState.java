package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

public class CompilerState {
	
	private LinkedList<ParserRuleContext> executionStackTree
		= new LinkedList<ParserRuleContext>();

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
		return classContext.get(classContext.size()-n);
	}
	
	private LinkedList<ResolveContext> resolveContext = new LinkedList<ResolveContext>();

	public void pushResolveContext() {
		resolveContext.add(new ResolveContext());
	}
	
	public void popResolveContext() {
		resolveContext.pop();
	}
	
	public TypeInformation resolveType(String identifier) {
		List<ResolveContext> reverse = new ArrayList<ResolveContext>(resolveContext);
		for (ResolveContext rc : reverse) {
			if (rc.hasIdentifier(identifier)) {
				return rc.resolveType(identifier);
			}
		}
		return null;
	}
	
	public void addType(TypeInformation type, String identifier) {
		resolveContext.peekLast().addType(type, identifier);
	}
}
