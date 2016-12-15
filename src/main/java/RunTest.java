

import java.io.File;

import cz.upol.prf.vanusanik.pdlang.cl.PDLangClassLoader;

public class RunTest {
	
	public static void main(String[] args) throws ClassNotFoundException {
		File f = new File("src/test/pdl");
		System.out.println(f.getAbsolutePath());
		
		PDLangClassLoader c = new PDLangClassLoader(Thread.currentThread().getContextClassLoader());
		
		c.addBuildPath(f);
		
		c.setDebug(true);
		Class<?> i = c.loadClass("~pd~invaIi");
		System.out.println(i);
		i = c.loadClass("~pd~invaIi⟪D⟫");
		System.out.println(i);
		i = c.loadClass("~pd~invD");
		System.out.println(i);
		i = c.loadClass("~pd~invT");
		System.out.println(i);
		i = c.loadClass("~pd~invTTaBb⟪T⟫");
		System.out.println(i);
		i = c.loadClass("~pd~inv⟪T⟫");
		System.out.println(i);
		
		c.loadClass("~pdtest/test_module");
	}

}
