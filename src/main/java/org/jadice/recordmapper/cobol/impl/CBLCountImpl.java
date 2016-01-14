package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLCount;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;

public class CBLCountImpl extends CBLNumericImpl {
	private CBLCount spec;
	private CBLTableImpl referencedFieldMapping;

	protected void init(Annotation a) {
		this.spec = (CBLCount) a;
	}

	@Override
	protected void postInit() throws MappingException {
		super.postInit();

		FieldMapping fm = recordMapping.getFieldMapping(spec.ref());
		if (null == fm)
			throw new MappingException(this, "The referenced field " + spec.ref()
					+ " could not be found");

		if (!(fm instanceof CBLTableImpl))
			throw new MappingException(this, "The referenced field " + spec.ref()
					+ " is not an array or collection annotated with @CBLTable");

		referencedFieldMapping = (CBLTableImpl) fm;
		referencedFieldMapping.registerParameterField(this);
	}

	public int getSize(MappingContext ctx) {
		return spec.length();
	}

}
