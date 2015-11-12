package org.jadice.recordmapper;

import org.jadice.recordmapper.impl.MappingContext;

public abstract class Mapping {
	public abstract int getSize(MappingContext ctx) throws MappingException;
}
