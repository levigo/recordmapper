package org.jadice.recordmapper.cobol;

import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public interface Discriminator {
  void init(Class<?> recordClass, Field field) throws MappingException;

  Class<?> getComponentType(MappingContext ctx) throws MappingException;

  void beforeMarshal(MarshalContext ctx) throws MappingException;

  void afterUnmarshal(UnmarshalContext ctx) throws MappingException;
}
