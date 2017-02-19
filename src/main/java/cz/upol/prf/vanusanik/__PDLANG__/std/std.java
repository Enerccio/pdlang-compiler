package cz.upol.prf.vanusanik.__PDLANG__.std;

import cz.upol.prf.vanusanik.pdlang.external.CurrentCallContext;
import cz.upol.prf.vanusanik.pdlang.external.PDLangExternalElement;
import cz.upol.prf.vanusanik.pdlang.external.PDLangForeignFunction;
import cz.upol.prf.vanusanik.pdlang.external.PDLangForeignType;

public class std {

	public static class System implements PDLangForeignType {
		private static final long serialVersionUID = -2748498598704003075L;

	}
	
	public static class SystemInstanceMethod implements PDLangExternalElement, PDLangForeignFunction {

		public Object invoke(CurrentCallContext ctx) {
			return null;
		}

	}
	
	public static class NodeCurrentTimeMS implements PDLangExternalElement, PDLangForeignFunction {

		public Object invoke(CurrentCallContext ctx) {
			return null;
		}

	}

}
