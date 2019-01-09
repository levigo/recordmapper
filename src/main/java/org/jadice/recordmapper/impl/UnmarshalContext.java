package org.jadice.recordmapper.impl;

import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.RecordAttributes;

public interface UnmarshalContext extends MappingContext {

  String getString(int size) throws MappingException;

  byte[] getBytes(int size) throws MappingException;

  public <T extends RecordAttributes> T getRecordAttributes(Class<T> c);

  void setValue(Field field, Object value) throws MappingException;

  UnmarshalContext createMemberContext(Object member, RecordMapping memberMapping);

  int getPosition();
}
