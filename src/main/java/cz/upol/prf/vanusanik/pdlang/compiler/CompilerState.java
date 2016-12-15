package cz.upol.prf.vanusanik.pdlang.compiler;

import java.util.LinkedList;

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
	
}
