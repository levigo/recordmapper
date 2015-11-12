package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLRecordAttributes;
import org.jadice.recordmapper.cobol.CBLSize;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLSizeImpl extends CBLNumericImpl {
	private CBLSize spec;
	private FieldMapping referencedFieldMapping;

	protected void init(Annotation a) {
		this.spec = (CBLSize) a;
	}

	@Override
	protected void postInit() throws MappingException {
		super.postInit();

		referencedFieldMapping = recordMapping.getFieldMapping(spec.ref());
		if (null == referencedFieldMapping)
			throw new MappingException(this, "The referenced field " + spec.ref()
					+ " could not be found");

		referencedFieldMapping.registerParameterField(this);
	}

	public int getSize(MappingContext ctx) {
		return spec.length();
	}

	public void marshal(MarshalContext ctx, Object value) throws MappingException {
		marshal(ctx, //
				referencedFieldMapping.getSize(ctx),//
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
