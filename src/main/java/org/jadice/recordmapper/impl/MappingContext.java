package org.jadice.recordmapper.impl;

import java.lang.reflect.Field;

import org.jadice.recordmapper.Mapping;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.RecordAttributes;

public interface MappingContext {
  Object getRecord();

  <T extends RecordAttributes> T getRecordAttributes(Class<T> c);

  Object getValue(Field field) throws MappingException;

  Mapping getTarget(String ref);
}
