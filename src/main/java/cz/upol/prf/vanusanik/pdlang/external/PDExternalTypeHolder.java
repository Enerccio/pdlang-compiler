package cz.upol.prf.vanusanik.pdlang.external;

public class PDExternalTypeHolder<T extends PDLangForeignType> implements PDLangExternalElement {

	private Class<T> foreignType;

	public PDExternalTypeHolder(Class<T> clazz) {
		this.foreignType = clazz;
	}

	public Class<T> getType() {
		return this.foreignType;
	}

}
