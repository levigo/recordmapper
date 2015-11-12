package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLString;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLStringImpl extends FieldMapping {
	private String padding;

	private CBLString spec;

	protected void init(Annotation a) {
		this.spec = (CBLString) a;

		char p[] = new char[spec.value()];
		Arrays.fill(p, spec.pad());

		padding = new String(p);
	}

	public int getSize(MappingContext ctx) {
		return spec.value();
	}

	public void marshal(MarshalContext ctx, Object value) throws MappingException {
		ctx.put(adjustLength(null != value ? (String) value : "", ctx));
	}

	private String adjustLength(String value, MappingContext ctx) throws MappingException {
		int expected = getSize(ctx);
		int actual = value.length();

		if (actual > expected) {
		    if (spec.truncate()) { 
		      value = value.substring(0, expected);
		      actual = expected;
		    } else
          throw new MappingException("Length of value " + value + " for field " + field + " exceeds allowed length("
          		+ expected + ")");
    }

		while (actual < expected) {
			int pad = Math.min(padding.length(), expected - actual);
			value += padding.substring(0, pad);
			actual += pad;
		}

		return value;
	}

	public Object unmarshal(UnmarshalContext ctx) throws MappingException {
		String s = ctx.getString(getSize(ctx));
		return s.trim();
	}
}
