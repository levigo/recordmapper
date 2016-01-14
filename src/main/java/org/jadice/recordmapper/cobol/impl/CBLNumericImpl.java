package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLNumeric;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLNumericImpl extends FieldMapping {
	private CBLNumeric spec;

	protected void init(Annotation a) {
		this.spec = (CBLNumeric) a;
	}

	public int getSize(MappingContext ctx) {
		return spec.value();
	}

  public void marshal(MarshalContext ctx, Object value) throws MappingException {
    ctx.put(adjustLength(null != value ? String.valueOf(value) : "0", ctx));
  }

  private String adjustLength(String value, MappingContext ctx) throws MappingException {
    int expected = getSize(ctx);
    int actual = value.length();

    if (actual > expected) {
          throw new MappingException("Length of value " + value + " for field " + field + " exceeds allowed length("
              + expected + ")");
    }

    StringBuffer adjustedValue = new StringBuffer(value);
    while (actual < expected) {
      adjustedValue.insert(0, 0);
      actual = adjustedValue.length();
    }

    return adjustedValue.toString();
  }



	public Object unmarshal(UnmarshalContext ctx) throws MappingException {
	  return Long.valueOf(ctx.getString(getSize(ctx)));
	}


}
