package org.jadice.recordmapper.impl.dflt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import org.jadice.recordmapper.BaseRecordAttributes;
import org.jadice.recordmapper.Mapping;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.RecordAttributes;
import org.jadice.recordmapper.impl.RecordMapping;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class DefaultUnmarshalContext implements UnmarshalContext {
  private final UnmarshalSource src;

  private final DefaultUnmarshaller unmarshaller;
  private final RecordMapping recordMapping;
  private final Object record;

  public DefaultUnmarshalContext(UnmarshalSource src, DefaultUnmarshaller unmarshaller, RecordMapping recordMapping,
      Object record) {
    this.src = src;

    this.unmarshaller = unmarshaller;
    this.recordMapping = recordMapping;
    this.record = record;
  }

  public DefaultUnmarshalContext(DefaultUnmarshalContext parent, RecordMapping recordMapping, Object record) {
    this.src = parent.src;
    this.unmarshaller = parent.unmarshaller;
    this.recordMapping = recordMapping;
    this.record = record;
  }

  public <T extends RecordAttributes> T getRecordAttributes(Class<T> c) {
    return unmarshaller.getRecordAttributes(c);
  }

  public Mapping getTarget(String ref) {
    return recordMapping.getFieldMapping(ref);
  }

  public Object getValue(Field field) throws MappingException {
    try {
      return field.get(record);
    } catch (Exception e) {
      throw new MappingException(e);
    }
  }

  public void setValue(Field field, Object value) throws MappingException {
    try {
      field.set(record, prepareValue(field, value));
    } catch (Exception e) {
      throw new MappingException("Can't set field value for " + field, e);
    }
  }

  /**
   * Prepare value so that {@link Field#set(Object, Object)} works smoothly.
   * 
   * @param field
   * @param value
   * @return
   */
  private Object prepareValue(Field field, Object value) {
    Class<?> t = field.getType();

    if (t.equals(byte.class) || t.equals(Byte.class))
      value = ((Number) value).byteValue();
    else if (t.equals(short.class) || t.equals(Short.class))
      value = ((Number) value).shortValue();
    else if (t.equals(int.class) || t.equals(Integer.class))
      value = ((Number) value).intValue();
    else if (t.equals(long.class) || t.equals(Long.class))
      value = ((Number) value).longValue();
    else if (t.equals(float.class) || t.equals(Float.class))
      value = ((Number) value).floatValue();
    else if (t.equals(double.class) || t.equals(Double.class))
      value = ((Number) value).doubleValue();

    return value;
  }

  public UnmarshalContext createMemberContext(Object member, RecordMapping memberMapping) {
    return new DefaultUnmarshalContext(this, memberMapping, member);
  }

  public Object getRecord() {
    return record;
  }

  public byte[] getBytes(int size) throws MappingException {
    return src.getBytes(size);
  }

  public int getPosition() {
    return src.getPosition();
  }

  public String getString(int size) throws MappingException {
    try {
      return new String(getBytes(size), getRecordAttributes(BaseRecordAttributes.class).getEncoding());
    } catch (UnsupportedEncodingException e) {
      throw new MappingException(e);
    }
  }

  @Override
  public boolean hasMore() throws MappingException {
    try {
      return src.hasMore();
    } catch (IOException e) {
      throw new MappingException( e);
    }
  }
}
