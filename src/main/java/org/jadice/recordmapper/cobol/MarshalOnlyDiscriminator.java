package org.jadice.recordmapper.cobol;

import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

@SuppressWarnings("rawtypes")
public class MarshalOnlyDiscriminator implements Discriminator {

  @Override
  public void init(Class recordClass, Field field) throws MappingException {
    // nothing to do
  }

  @Override
  public Class<?> getComponentType(MappingContext ctx) throws MappingException {
    throw new UnsupportedOperationException(
        "This discriminator can't be used for unmarshalling");
  }

  @Override
  public void beforeMarshal(MarshalContext ctx) throws MappingException {
    // nothing to do
  }

  @Override
  public void afterUnmarshal(UnmarshalContext ctx) throws MappingException {
    // nothing to do
  }
}
