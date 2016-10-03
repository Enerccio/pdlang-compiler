package cz.upol.prf.vanusanik.pdlang.external;

import java.util.ArrayList;
import java.util.List;

public abstract class EFunction {	
	
	public static class EFunctionReturn {
		public Object returnVal;
	}
	
	public static class EFunctionArgs {
		
	}
	
	private boolean varargs = false;
	private ETypeDescriptor varargsType;
	private List<ETypeDescriptor> types = new ArrayList<ETypeDescriptor>();
	
	@SuppressWarnings("unchecked")
	public <T extends EFunction> T setVarargs(ETypeDescriptor descriptor) {
		varargs = true;
		varargsType = descriptor;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EFunction> T addParameter(ETypeDescriptor type) {
		types.add(type);
		return (T) this;
	}
	
	protected abstract int fixedArgsCount();
	
	protected abstract boolean hasVarargs();
	
	protected abstract EFunctionReturn run(EFunctionArgs args);
	
}
