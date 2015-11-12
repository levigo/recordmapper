package org.jadice.recordmapper.impl.dflt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.jadice.recordmapper.BaseRecordAttributes;
import org.jadice.recordmapper.Mapping;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.RecordAttributes;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.RecordMapping;

public class DefaultMarshalContext implements MarshalContext {

	private final DefaultMarshaller marshaller;
	private final OutputStream os;
	private final Object record;
	private final RecordMapping recordMapping;

	private final Map<Object, DefaultMarshalContext> memberContexts = new HashMap<Object, DefaultMarshalContext>();

	public DefaultMarshalContext(DefaultMarshalContext parent,
			DefaultMarshaller marshaller, RecordMapping recordMapping, Object record,
			OutputStream os) {
		this.marshaller = marshaller;
		this.recordMapping = recordMapping;
		this.record = record;
		this.os = os;
	}

	public <T extends RecordAttributes> T getRecordAttributes(Class<T> c) {
		return marshaller.getRecordAttributes(c);
	}

	public void put(String s) throws MappingException {
		BaseRecordAttributes bra = getRecordAttributes(BaseRecordAttributes.class);
		try {
			String e = bra.getEncoding();
			put(s.getBytes(e));
		} catch (UnsupportedEncodingException e) {
			throw new MappingException( e);
		}
	}

	public void put(byte[] buffer) throws MappingException {
		try {
			os.write(buffer);
		} catch (IOException e) {
			throw new MappingException( e);
		}
	}

	public void put(byte b) throws MappingException {
		try {
			os.write(b);
		} catch (IOException e) {
			throw new MappingException( e);
		}
	}

	public Object getValue(Field fieldRef) throws MappingException {
		try {
			return fieldRef.get(record);
		} catch (Exception e) {
			throw new MappingException( e);
		}
	}

	public Mapping getTarget(String ref) {
		return recordMapping.getFieldMapping(ref);
	}

	public MarshalContext getMemberContext(Object record) {
		DefaultMarshalContext mc = memberContexts.get(record);
		if (null == mc) {
			mc = new DefaultMarshalContext(this, marshaller, recordMapping, record,
					os);
			memberContexts.put(record, mc);
		}

		return mc;
	}

	public Object getRecord() {
		return record;
	}
}
