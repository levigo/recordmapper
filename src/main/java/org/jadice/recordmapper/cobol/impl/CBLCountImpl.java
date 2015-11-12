package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLCount;
import org.jadice.recordmapper.cobol.CBLRecordAttributes;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

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

	public void marshal(MarshalContext ctx, Object value) throws MappingException {
		marshal(ctx, //
				referencedFieldMapping.getCount(ctx),//
				ctx.getRecordAttributes(CBLRecordAttributes.class).getEndian(), //
				false);
	}

	public Object unmarshal(UnmarshalContext ctx) throws MappingException {
		return unmarshal(ctx, //
				ctx.getBytes(getSize(ctx)), //
				ctx.getRecordAttributes(CBLRecordAttributes.class).getEndian(),//
				false);
	}
}
