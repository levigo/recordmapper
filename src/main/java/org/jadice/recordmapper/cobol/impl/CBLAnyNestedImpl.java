package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLAnyNested;
import org.jadice.recordmapper.cobol.Discriminator;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.RecordMapping;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLAnyNestedImpl extends FieldMapping {

  private CBLAnyNested spec;

  private Discriminator discriminator;

  @Override
  protected void init(final Annotation a) throws MappingException {
    spec = (CBLAnyNested) a;

    try {
      discriminator = spec.value().newInstance();

      discriminator.init(recordMapping.getRecordClass(), field);
    } catch (final Exception e) {
      throw new MappingException(this, "Invalid @CBLAnyNested", e);
    }
  }

  @Override
  public Collection<? extends Class<?>> getReferencedClasses() {
    return Collections.emptyList();
  }

  @Override
  public int getSize(final MappingContext ctx) throws MappingException {
    if (!(ctx instanceof MarshalContext))
      throw new MappingException(this, "I am not supposed to know my size at this point");

    return getComponentMapping(ctx).getSize(((MarshalContext) ctx).getMemberContext(ctx.getValue(field)));
  }

  private RecordMapping getComponentMapping(final MarshalContext ctx, final Object value) throws MappingException {
    if (null != value) {
      final RecordMapping valueMapping = recordMapping.getRecordMapping(value.getClass());
      if (null != valueMapping)
        return valueMapping;
    }

    return getComponentMapping(ctx);
  }

  private RecordMapping getComponentMapping(final MappingContext ctx) throws MappingException {
    final Class<?> c = discriminator.getComponentType(ctx);
    final RecordMapping componentMapping = recordMapping.getRecordMapping(c);
    if (null == componentMapping)
      throw new MappingException(this, "Discriminator did not return a component type for " + field);
    return componentMapping;
  }

  @Override
  public void marshal(final MarshalContext ctx, final Object value) throws MappingException {
    getComponentMapping(ctx, value).marshal(value, ctx.getMemberContext(value));
  }

  @Override
  public Object unmarshal(final UnmarshalContext ctx) throws MappingException {
    Object component;
    try {
      final RecordMapping componentMapping = getComponentMapping(ctx);

      component = componentMapping.getRecordClass().newInstance();
      final UnmarshalContext mc = ctx.createMemberContext(component, componentMapping);
      componentMapping.unmarshal(component, mc);

      return component;
    } catch (final Exception e) {
      throw new MappingException(this, e);
    }
  }

  @Override
  public void registerParameterField(final FieldMapping param) throws MappingException {
    // we don't care, but let a size field have its way anyway.
  }

  @Override
  public void beforeMarshal(final MarshalContext mc) throws MappingException {
    discriminator.beforeMarshal(mc);
  }

  @Override
  public void afterUnmarshal(final UnmarshalContext mc) throws MappingException {
    discriminator.afterUnmarshal(mc);
  }
}
