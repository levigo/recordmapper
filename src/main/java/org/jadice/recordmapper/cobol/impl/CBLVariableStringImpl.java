package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLVariableString;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLVariableStringImpl extends FieldMapping {
	private static final String BLANKS = "                                                            ";
	
  private CBLVariableString spec;

  private FieldMapping sizeField;

	protected void init(Annotation a) {
		this.spec = (CBLVariableString) a;
	}

	@Override
	protected void postInit() throws MappingException {
	  super.postInit();
	  
	  if (spec.sizeRef().length() > 0) {
      sizeField = recordMapping.getFieldMapping(spec.sizeRef());
    } 
	  
	  if(null == sizeField)
      throw new MappingException(this, "Must have a valid 'sizeRef'");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.nuernberger.util.recordmapper.impl.FieldMapper#getSize(de.nuernberger.util.recordmapper.impl.MappingContext)
	 */
	public int getSize(MappingContext ctx) throws MappingException {
		Object value = ctx.getValue(field);
		return null != value ? value.toString().length() : 0;
	}

	@Override
	public void beforeMarshal(MarshalContext mc) throws MappingException {
	  super.beforeMarshal(mc);
	  
	  if (null == sizeField)
      throw new MappingException(this, "Can't marshal: no size field");
	  
	  setIntegerFieldValue(mc, sizeField, getSize(mc));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.nuernberger.util.recordmapper.impl.FieldMapper#marshal(de.nuernberger.util.recordmapper.impl.MarshalContext,
	 *      java.lang.Object)
	 */
	public void marshal(MarshalContext ctx, Object value) throws MappingException {
		ctx.put(adjustLength((String) value, ctx));
	}

	private String adjustLength(String value, MappingContext ctx)
			throws MappingException {
		int expected = getSize(ctx);
		int actual = value.length();

		if (actual > expected)
			return value.substring(0, expected);

		while (actual < expected) {
			int pad = Math.min(BLANKS.length(), expected - actual);
			value += BLANKS.substring(0, pad);
			actual += pad;
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.nuernberger.util.recordmapper.impl.FieldMapper#unmarshal(de.nuernberger.util.recordmapper.impl.UnmarshalContext)
	 */
	public Object unmarshal(UnmarshalContext ctx) throws MappingException {
		if (null == sizeField)
			throw new MappingException(this, "Can't unmarshal: no size field");

		return ctx.getString(
				((Number) ctx.getValue(sizeField.getField())).intValue()).trim();
	}
}
