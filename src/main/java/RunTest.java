
import java.io.File;

import cz.upol.prf.vanusanik.PDLang;
import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;
import cz.upol.prf.vanusanik.pdlang.external.ExternalModuleHolder;

public class RunTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws ClassNotFoundException {
		File f = new File("src/test/pdl");
		System.out.println(f.getAbsolutePath());

		PDLang ctx = PDLang.getHandle();
		ctx.init(Thread.currentThread().getContextClassLoader());

		PDLangClassLoader c = ctx.getClassLoader();
		c.setDebug(true);

		ExternalModuleHolder h = ctx.getModule("std.std");

		// c.loadClass("~pdtest/test_module");;
	}

}
