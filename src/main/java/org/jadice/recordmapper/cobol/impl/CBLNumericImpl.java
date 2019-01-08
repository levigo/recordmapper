package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;

import org.jadice.recordmapper.Endian;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLNumeric;
import org.jadice.recordmapper.cobol.CBLRecordAttributes;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLNumericImpl extends FieldMapping {
	private CBLNumeric spec;

	protected void init(final Annotation a) {
		this.spec = (CBLNumeric) a;
	}

	public int getSize(final MappingContext ctx) {
		return spec.value();
	}

	public void marshal(final MarshalContext ctx, final Object value) throws MappingException {
		marshal(ctx, //
				((Number) value).longValue(), //
				ctx.getRecordAttributes(CBLRecordAttributes.class).getEndian(), //
				spec.signed());
	}

	protected void marshal(final MarshalContext ctx, final long v, final Endian endian,
			final boolean isSigned) throws MappingException {
		switch (getSize(ctx)){
			case 1 :
				if ((isSigned && (v > Byte.MAX_VALUE || v < Byte.MIN_VALUE))
						|| (!isSigned && (v > 0xff || v < 0)))
					throwValueOutOfRange(ctx);
				ctx.put(isSigned ? (byte) v : (byte) (v & 0xff));
				break;
			case 2 :
				if ((isSigned && (v > Short.MAX_VALUE || v < Short.MIN_VALUE))
						|| (!isSigned && (v > 0xffff || v < 0)))
					throwValueOutOfRange(ctx);
				storeValue(ctx, endian, isSigned ? (short) v : (short) (v & 0xffff));
				break;
			case 4 :
				if ((isSigned && (v > Integer.MAX_VALUE || v < Integer.MIN_VALUE))
						|| (!isSigned && (v > 0xffffffffl || v < 0)))
					throwValueOutOfRange(ctx);
				storeValue(ctx, endian, isSigned ? (int) v : (int) (v & 0xffffffffl));
				break;
			case 8 :
				if (!isSigned)
					throw new MappingException(this, "Unsigned long value not supported");
				storeValue(ctx, endian, v);
				break;
			default :
				throw new MappingException(this, "Invalid length for a CBLNumeric");
		}
	}

	private void throwValueOutOfRange(final MarshalContext ctx) throws MappingException {
		throw new MappingException(
				this, "Numeric value out of range for field length of " + getSize(ctx));
	}

	private void storeValue(final MarshalContext ctx, final Endian endian,
			final long valueRespectingSign) throws MappingException {
		int s = getSize(ctx);
		byte buffer[] = new byte[s];

		int shift = 0;
		int offset = endian == Endian.BIG ? s : 0;
		int fillOrder = endian == Endian.BIG ? -1 : 1;
		for (int i = 0; i < s; i++) {
			offset += fillOrder;
			buffer[offset] = (byte) ((valueRespectingSign >> shift) & 0xff);
			shift += 8;
		}

		ctx.put(buffer);
	}

	public Object unmarshal(final UnmarshalContext ctx) throws MappingException {
		return unmarshal(ctx,//
				ctx.getBytes(getSize(ctx)), //
				ctx.getRecordAttributes(CBLRecordAttributes.class).getEndian(), //
				spec.signed());
	}

	protected Object unmarshal(final UnmarshalContext ctx, final byte[] b,
			final Endian endian, final boolean isSigned) throws MappingException {
		switch (getSize(ctx)){
			case 1 :
				long value = b[0];
				return isSigned ? value : value & 0xffl;
			case 2 :
				value = extractValue(b, endian, ctx);
				return isSigned ? value : value & 0xffffl;
			case 4 :
				value = extractValue(b, endian, ctx);
				return isSigned ? value : value & 0xffffffffl;
			case 8 :
				return extractValue(b, endian, ctx);
			default :
				throw new MappingException(this, "Invalid length for a CBLNumeric");
		}
	}

	private long extractValue(final byte[] byteArray, final Endian endian,
			final MappingContext ctx) {
		long result = 0;
		int shift = 0;
		int s = getSize(ctx);
		int offset = endian == Endian.BIG ? s : 0;
		int fillOrder = endian == Endian.BIG ? -1 : 1;
		for (int i = 0; i < s; i++) {
			offset += fillOrder;
			result |= (((long) byteArray[offset]) & 0xff) << shift;
			shift += 8;
		}

		return result;
	}
}
