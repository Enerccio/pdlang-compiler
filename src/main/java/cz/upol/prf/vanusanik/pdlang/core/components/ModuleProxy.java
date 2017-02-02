package cz.upol.prf.vanusanik.pdlang.core.components;

import java.io.Serializable;

import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.SystemException;

public class ModuleProxy implements Serializable {
	private static final long serialVersionUID = 8574467522147737587L;
	
	protected transient Module proxy;
	
	protected Class<? extends Module> proxyClass;
	protected ModuleParameter[] parameters;
	
	public ModuleProxy(Class<? extends Module> module, ModuleParameter... parameters) {
		this.proxyClass = module;
		this.parameters = parameters;
	}
	
	public Module get() {
		if (proxy == null) {
			synchronized (this) {
				if (proxy == null) {
					proxy = init();
				}
			}
		}
		return proxy;
	}

	/**
	 * Initializes the module from class and parameters
	 * @return
	 */
	protected Module init() {
		try {
			Module proxy = proxyClass.newInstance();
			// TODO: for every parameter, init module
			return proxy;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

}
