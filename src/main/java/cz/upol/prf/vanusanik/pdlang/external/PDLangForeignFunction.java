package cz.upol.prf.vanusanik.pdlang.external;

import cz.upol.prf.vanusanik.pdlang.tools.Stateless;

@Stateless
public interface PDLangForeignFunction extends PDLangExternalElement {

	public Object invoke(CurrentCallContext ctx);

}
