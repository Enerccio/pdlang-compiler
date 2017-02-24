package cz.upol.prf.vanusanik.pdlang.compiler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import cz.upol.inf.vanusanik.pdlang.parser.pdlangBaseVisitor;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangLexer;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ClosureFormalParamsContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ClosureParamContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ConstTypeContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ForeignMethodContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ForeignTypeContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ModuleDefinitionContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.ModuleFuncContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.SimpleImportContext;
import cz.upol.inf.vanusanik.pdlang.parser.pdlangParser.TypeContext;
import cz.upol.prf.vanusanik.PDLang;
import cz.upol.prf.vanusanik.pdlang.compiler.TypeInformation.Type;
import cz.upol.prf.vanusanik.pdlang.core.components.exceptions.CompilationException;
import cz.upol.prf.vanusanik.pdlang.external.PDExternalTypeHolder;
import cz.upol.prf.vanusanik.pdlang.external.PDLangExternalElement;
import cz.upol.prf.vanusanik.pdlang.external.PDLangForeignFunction;
import cz.upol.prf.vanusanik.pdlang.path.PDPathDescriptor;
import cz.upol.prf.vanusanik.pdlang.tools.Constants;
import cz.upol.prf.vanusanik.pdlang.tools.Utils;

/**
 * ModuleDiscoveryManager
 * 
 * Scans the path for all pdlang files and builds 
 * type information for the rest of the compilation
 * 
 * @author pvan
 */
public class ModuleDiscoveryManager {
	
	/**
	 * Tree type information enumerator
	 * @author pvan
	 *
	 */
	public static enum TreeType {
		UNUSED, MODULE, TYPE, FUNCTION
	}
	
	/**
	 * Stores type proxy in tree
	 * @author pvan
	 *
	 */
	private static abstract class TypeContainer {
		private TypeProxy proxy;

		public TypeProxy getProxy() {
			return proxy;
		}

		public void setProxy(TypeProxy proxy) {
			this.proxy = proxy;
		}
	}
	
	public static class UnusedTypeContainer extends TypeContainer {
		
	}
	
	public static class TypeTypeContainer extends TypeContainer {
		
	}

	/**
	 * Tree structure holding types via dotted path
	 */
	public static class TypeInfoHolder {
		/** Subtype map */
		private Map<String, TypeInfoHolder> subtypes = new HashMap<String, TypeInfoHolder>();
		
		/** Type type*/
		private final TreeType t;
		/** Actual type */
		private final TypeContainer type;
		
		public TypeInfoHolder(TreeType t, TypeContainer type) {
			this.t = t;
			this.type = type;
		}

		public TypeContainer getType() {
			return type;
		}

		public TreeType getT() {
			return t;
		}

		@Override
		public String toString() {
			return (t != TreeType.UNUSED) ? t.name() + ", " + subtypes.toString() : subtypes.toString();
			
		}		
	}

	/** Roots */
	private Map<String, TypeInfoHolder> types = new HashMap<String, TypeInfoHolder>();
	/** List of unresolved types that gets scanned after each package scan */
	private List<TypeProxy> unresolvedTypes = new ArrayList<TypeProxy>();
	/** List of non-created invokers */
	private List<TypeInformation> invokers = new ArrayList<TypeInformation>();
	
	/**
	 * Sets the package.type.subtype path for that holder
	 * @param path
	 * @param holder
	 */
	private void setPathElement(String path, TypeInfoHolder holder) {
		String[] elements = path.split(Pattern.quote("."));
		Map<String, TypeInfoHolder> lookup = types;
		for (int i=0; i<elements.length; i++) {
			String element = elements[i];
			if (i < elements.length-1) {
				if (!lookup.containsKey(element)) {
					lookup.put(element, new TypeInfoHolder(TreeType.UNUSED, new UnusedTypeContainer()));
				}
				lookup = lookup.get(element).subtypes;
			} else {
				lookup.put(element, holder);
			}
		}		
 	}
	
	/**
	 * Returns path element for package
	 * @param path
	 * @return
	 */
	private TypeInformation getPathElement(String path) {
		String[] elements = path.split(Pattern.quote("."));
		Map<String, TypeInfoHolder> lookup = types;
		for (int i=0; i<elements.length; i++) {
			String element = elements[i];
			if (i < elements.length-1) {
				if (!lookup.containsKey(element)) {
					return null;
				}
				lookup = lookup.get(element).subtypes;
			} else {
				TypeInfoHolder holder = lookup.get(element);
				if (holder == null)
					return null;
				try {
					return holder.type.getProxy().getType();
				} catch (NullPointerException e) {
					return null;
				}
			}
		}	
		return null;
	}
	
	/**
	 * Returns type information for path or null if no such type exists
	 * @param path
	 * @return
	 */
	public TypeInformation getType(String path) {
		return getPathElement(path);
	}

	/**
	 * Scans the path descriptor for types
	 * @param descriptor
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public void scan(final PDPathDescriptor descriptor, final PDLang context) {

		try {
			for (final String module : descriptor.getAvailableModules()) {
				final String modPath = Utils.slashes2dots(module);
				
				InputStream in = new ByteArrayInputStream(descriptor.getModule(module));

				ANTLRInputStream is = new ANTLRInputStream(in);
				pdlangLexer lexer = new pdlangLexer(is);
				lexer.removeErrorListeners();
				lexer.addErrorListener(new ThrowingErrorListener(module));
				CommonTokenStream stream = new CommonTokenStream(lexer);
				
				final Map<String, String> importResolves = new HashMap<String, String>();

				pdlangBaseVisitor<Void> visitor = new pdlangBaseVisitor<Void>() {					
					
					@Override
					public Void visitSimpleImport(SimpleImportContext ctx) {
						// Resolves imports
						
						String moduleName = ctx.moduleName().getText();
						String[] pathComponents = module.split(Pattern.quote("."));
						String modName = pathComponents[pathComponents.length-1];
						importResolves.put(modName, moduleName);
						return super.visitSimpleImport(ctx);
					}

					@Override
					public Void visitModuleDefinition(ModuleDefinitionContext ctx) {
						// Stores the module as type into package path
						
						TypeInformation ti = new TypeInformation();
						ti.setType(Type.MODULE);
						ti.setJavaTypeName(Constants.PD_CLASSTYPE + Utils.dots2slashes(modPath));
						
						TypeTypeContainer tc = new TypeTypeContainer();
						tc.setProxy(new TypeProxy(modPath));
						tc.getProxy().setType(ti);
						
						TypeInfoHolder h = new TypeInfoHolder(TreeType.MODULE, tc);
						
						setPathElement(modPath, h);
						
						return super.visitModuleDefinition(ctx);
					}

					@Override
					public Void visitForeignType(ForeignTypeContext ctx) {
						// Stores foreign type into package path
						
						String identifier = ctx.identifier().getText();
						String typePath = modPath + "." + identifier;
						
						PDLangExternalElement e = context.getForeignBinding(typePath);
						if (e == null || !(e instanceof PDExternalTypeHolder)) {
							throw new CompilationException("Failed to find foreign type " + typePath);
						}
						
						PDExternalTypeHolder<?> extType = (PDExternalTypeHolder<?>)e;
						
						TypeInformation ti = new TypeInformation();
						ti.setType(Type.FOREIGN);
						ti.setJavaClassName(extType.getType().getName());
						ti.setJavaTypeName("L"+ Utils.dots2slashes(extType.getType().getName()) + ";");
						
						TypeTypeContainer tc = new TypeTypeContainer();
						tc.setProxy(new TypeProxy(typePath));
						tc.getProxy().setType(ti);
						
						TypeInfoHolder h = new TypeInfoHolder(TreeType.TYPE, tc);
						
						setPathElement(typePath, h);
						
						return super.visitForeignType(ctx);
					}					

					@Override
					public Void visitModuleFunc(ModuleFuncContext ctx) {						
						// Stores module defined function into type path
						
						String identifier = ctx.identifier().getText();
						String typePath = modPath + "." + identifier;
						
						TypeInformation ti = new TypeInformation();
						
						if (ctx.staticFunc() != null) {
							// Static function class type 
							// Static function does not have class type, since it is a static method on 
							//  module and can't be passed as invokers etc
							ti.setType(Type.STATIC_FUNCTION);
						} else {
							// Function class type
							String genname = Constants.PD_CLASSTYPE + Utils.dots2slashes(modPath) + Constants.PD_SEPARATOR + identifier + "Func";
							
							ti.setType(Type.FUNCTION);
							ti.setJavaClassName(genname);
							ti.setJavaTypeName("L"+ genname + ";");
						}
						
						// List of argument types+return type, return is first in this list
						List<TypeProxy> pList = new ArrayList<TypeProxy>();
						if (ctx.closure().closureRet() != null)
							pList.addAll(findTypes(Arrays.asList(ctx.closure().closureRet().type())));
						else {
							// no return means Object
							TypeInformation ti2 = new TypeInformation();
							ti2.setType(Type.BASIC_OBJECT);
							ti2.setJavaClassName(Object.class.getName());
							ti2.setJavaTypeName("L" + Utils.dots2slashes(Object.class.getName()) + ";");
							TypeProxy tp = new TypeProxy(null);
							tp.setType(ti2);
							pList.add(tp);
						}
							
						if (ctx.closure().closureParams() != null && ctx.closure().closureParams().closureFormalParams() != null) {
							ClosureFormalParamsContext fpctx = ctx.closure().closureParams().closureFormalParams();
							for (ClosureParamContext pctx : fpctx.closureParam()) {
								pList.addAll(findTypes(Arrays.asList(pctx.type())));
							}
						}
						ti.setCarryData(pList);
						
						if (ctx.staticFunc() == null) {
							// static functions can't have invokers
							invokers.add(ti);
						}
						
						TypeTypeContainer tc = new TypeTypeContainer();
						tc.setProxy(new TypeProxy(typePath));
						tc.getProxy().setType(ti);
						
						TypeInfoHolder h = new TypeInfoHolder(TreeType.FUNCTION, tc);
						
						setPathElement(typePath, h);
						
						return super.visitModuleFunc(ctx);
					}

					@Override
					public Void visitForeignMethod(ForeignMethodContext ctx) {
						// Foreign function, always static
						
						String identifier = ctx.identifier().getText();
						String typePath = modPath + "." + identifier;
						
						PDLangExternalElement e = context.getForeignBinding(typePath);
						if (e == null || !(e instanceof PDLangForeignFunction)) {
							throw new CompilationException("Failed to find foreign method " + typePath);
						}
						
						PDLangForeignFunction func = (PDLangForeignFunction)e;
						
						TypeInformation ti = new TypeInformation();
						ti.setType(Type.FOREIGN_FUNC);
						ti.setJavaClassName(func.getClass().getName());
						ti.setJavaTypeName("L"+ Utils.dots2slashes(func.getClass().getName()) + ";");
						
						List<TypeProxy> pList = new ArrayList<TypeProxy>();
						if (ctx.closureRet() != null)
							pList.addAll(findTypes(Arrays.asList(ctx.closureRet().type())));
						else {
							TypeInformation ti2 = new TypeInformation();
							ti2.setType(Type.BASIC_OBJECT);
							ti2.setJavaClassName(Object.class.getName());
							ti2.setJavaTypeName("L" + Utils.dots2slashes(Object.class.getName()) + ";");
							TypeProxy tp = new TypeProxy(null);
							tp.setType(ti2);
							pList.add(tp);
						}
							
						if (ctx.closureParams() != null && ctx.closureParams().closureFormalParams() != null) {
							ClosureFormalParamsContext fpctx = ctx.closureParams().closureFormalParams();
							for (ClosureParamContext pctx : fpctx.closureParam()) {
								pList.addAll(findTypes(Arrays.asList(pctx.type())));
							}
						}
						ti.setCarryData(pList);
						
						TypeTypeContainer tc = new TypeTypeContainer();
						tc.setProxy(new TypeProxy(typePath));
						tc.getProxy().setType(ti);
						
						TypeInfoHolder h = new TypeInfoHolder(TreeType.FUNCTION, tc);
						
						setPathElement(typePath, h);
						
						return super.visitForeignMethod(ctx);
					}

					private List<TypeProxy> findTypes(List<TypeContext> types) {
						// Converts TypeContext into TypeProxy
						
						List<TypeProxy> pList = new ArrayList<TypeProxy>();
						
						outer:
						for (TypeContext ctx : types) {
							if (ctx.constType() != null) {
								ConstTypeContext constType = ctx.constType();
								String type = constType.getText();
								if ("any".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Object.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Object.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("int".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(int.class.getName());
									ti.setJavaTypeName("I");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("lng".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(long.class.getName());
									ti.setJavaTypeName("J");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("flt".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(float.class.getName());
									ti.setJavaTypeName("F");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("dbl".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(double.class.getName());
									ti.setJavaTypeName("D");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("bol".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(boolean.class.getName());
									ti.setJavaTypeName("Z");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("chr".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC);
									ti.setJavaClassName(char.class.getName());
									ti.setJavaTypeName("C");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Int".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Integer.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Integer.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Lng".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Long.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Long.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Flt".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Float.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Float.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Dbl".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Double.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Double.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Bol".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Boolean.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Boolean.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("Chr".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(Character.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(Character.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
								
								if ("str".equals(type)) {
									TypeInformation ti = new TypeInformation();
									ti.setType(Type.BASIC_OBJECT);
									ti.setJavaClassName(String.class.getName());
									ti.setJavaTypeName("L" + Utils.dots2slashes(String.class.getName()) + ";");
									TypeProxy p = new TypeProxy(null);
									p.setType(ti);
									pList.add(p);
									continue;
								}
							}
							
							if (ctx.structType() != null) {
								String path = ctx.getText();
								for (String importResolve : importResolves.keySet()) {
									String[] components = importResolve.split(Pattern.quote("."));
									if (path.startsWith(components[0])) {										
										TypeProxy p = new TypeProxy(importResolves.get(importResolve) + "." + components[1]);
										pList.add(p);
										unresolvedTypes.add(p);
										continue outer;
									}
								}
								
								TypeProxy p = new TypeProxy(modPath + "." + path);
								pList.add(p);
								unresolvedTypes.add(p);
								continue;
							}
							
							if (ctx.invokerType() != null) {
								List<TypeContext> ctx2 = new ArrayList<TypeContext>();
								
								ctx2.add(ctx.invokerType().type());
								ctx2.addAll(ctx.invokerType().invokerArgTypes().type());
								
								TypeInformation ti = new TypeInformation();
								ti.setType(Type.FUNCTION);
								ti.setCarryData(findTypes(ctx2));
								TypeProxy p = new TypeProxy(null);
								p.setType(ti);
								invokers.add(ti);
								pList.add(p);
							}
						}
						
						return pList;
					}

				};

				pdlangParser parser = new pdlangParser(stream);
				parser.removeErrorListeners();
				parser.addErrorListener(new ThrowingErrorListener(module));

				visitor.visit(parser.compilationUnit());
			}
			
			// Resolve unresolved proxies
			for (TypeProxy proxy : unresolvedTypes) {
				if (!proxy.isResolved()) {
					proxy.setType(getPathElement(proxy.getExpectedPath()));
				}
			}
			
			// create invoker type names for invokers
			outer:
			for (TypeInformation invoker : invokers) {
				if (invoker.invokerType() == null) {
					List<TypeProxy> proxies = (List<TypeProxy>)invoker.getCarryData();
					for (TypeProxy p : proxies) {
						if (!p.isResolved())
							continue outer;
					}
					// TODO: create invokers
				}
			}
		} catch (Exception e) {
			throw new CompilationException(e);
		}

	}

}
