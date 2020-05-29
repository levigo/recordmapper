package org.jadice.recordmapper.cobol;

import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public interface Discriminator {
  default void init(Class<?> recordClass, Field field) throws MappingException {
    // nothing to do
  };

  Class<?> getComponentType(MappingContext ctx) throws MappingException;

  default void beforeMarshal(MarshalContext ctx) throws MappingException {
    // nothing to do
  };

  default void afterUnmarshal(UnmarshalContext ctx) throws MappingException {
    // nothing to do
  };
}
