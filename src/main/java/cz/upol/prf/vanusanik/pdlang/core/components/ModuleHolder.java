package cz.upol.prf.vanusanik.pdlang.core.components;

import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.SystemException;
import cz.upol.prf.vanusanik.pdlang.external.ExternalModuleHolder;

public class ModuleHolder implements ExternalModuleHolder {
	
	private Object instance;
	
	public ModuleHolder(Class<?> moduleClass) {
		try {
			instance = moduleClass.newInstance();	
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public Object get(String name) {
		try {
			return instance.getClass().getField(name).get(instance);
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

}
