package cz.upol.prf.vanusanik.__PDLANG__;

import cz.upol.prf.vanusanik.PDLang;
import cz.upol.prf.vanusanik.PDLangLibWrapper;
import cz.upol.prf.vanusanik.__PDLANG__.std.std;
import cz.upol.prf.vanusanik.pdlang.external.PDExternalTypeHolder;

public class StdlibLoader implements PDLangLibWrapper {

	public void addBindings(PDLang context) {
		context.setForeignBinding("std.std.System", new PDExternalTypeHolder<std.System>(std.System.class));
		
		context.setForeignBinding("std.std.systemInstance", new std.SystemInstanceMethod());
		context.setForeignBinding("std.std.nodeCurrentTimeMS", new std.NodeCurrentTimeMS());
	}

}
