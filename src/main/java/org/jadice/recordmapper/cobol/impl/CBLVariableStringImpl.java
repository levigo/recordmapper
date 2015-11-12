package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLVariableStringImpl extends FieldMapping {
	private static final String BLANKS = "                                                            ";
	private CBLSizeImpl sizeField;

	protected void init(Annotation a) {
		// this.spec = (CBLVariableString) a;
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

	@Override
	public void registerParameterField(FieldMapping param)
			throws MappingException {
		if (!(param instanceof CBLSizeImpl))
			throw new MappingException(this, "Unexpected parameterization of type " + param);

		this.sizeField = (CBLSizeImpl) param;
	}
}
