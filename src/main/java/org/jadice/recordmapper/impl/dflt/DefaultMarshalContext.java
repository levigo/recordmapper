package org.jadice.recordmapper.impl.dflt;

import java.io.IOException;
import java.io.OutputStream;
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

  public DefaultMarshalContext(final DefaultMarshalContext parent, final DefaultMarshaller marshaller, final RecordMapping recordMapping,
      final Object record, final OutputStream os) {
    this.marshaller = marshaller;
    this.recordMapping = recordMapping;
    this.record = record;
    this.os = os;
  }

  public <T extends RecordAttributes> T getRecordAttributes(final Class<T> c) {
    return marshaller.getRecordAttributes(c);
  }

  public void put(final String s) throws MappingException {
    put(s.getBytes(getRecordAttributes(BaseRecordAttributes.class).getCharset()));
  }

  public void put(final byte[] buffer) throws MappingException {
    try {
      os.write(buffer);
    } catch (IOException e) {
      throw new MappingException("Can't write", e);
    }
  }

  public void put(final byte b) throws MappingException {
    try {
      os.write(b);
    } catch (IOException e) {
      throw new MappingException("Can't write", e);
    }
  }

  public Object getValue(final Field fieldRef) throws MappingException {
    try {
      return fieldRef.get(record);
    } catch (Exception e) {
      throw new MappingException("Can't get value from " + fieldRef, e);
    }
  }

  public Mapping getTarget(final String ref) {
    return recordMapping.getFieldMapping(ref);
  }

  public MarshalContext getMemberContext(final Object record) {
    DefaultMarshalContext mc = memberContexts.get(record);
    if (null == mc) {
      mc = new DefaultMarshalContext(this, marshaller, recordMapping, record, os);
      memberContexts.put(record, mc);
    }

    return mc;
  }

  public Object getRecord() {
    return record;
  }
}
