package org.jadice.recordmapper.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.jadice.recordmapper.Mapping;
import org.jadice.recordmapper.MappingException;

public abstract class FieldMapping extends Mapping {
  public static Collection<Class<?>> EMPTY_REF = Collections.emptyList();

	protected RecordMapping recordMapping;
	protected Field field;
	protected Annotation fieldAnnotation;

	public abstract void marshal(MarshalContext ctx, Object value)
			throws MappingException;

	public abstract Object unmarshal(UnmarshalContext ctx)
			throws MappingException;

	protected final void init(RecordMapping recordMapping, Field f, Annotation a)
			throws MappingException {
		this.recordMapping = recordMapping;
		field = f;
		fieldAnnotation = a;

		init(a);
	}

	protected abstract void init(Annotation a) throws MappingException;

  public Collection<? extends Class<?>> getReferencedClasses() {
		// default: no refs
		return EMPTY_REF;
	}

	protected void postInit() throws MappingException {
		// default: do nothing
	}

	public Field getField() {
		return field;
	}

	public void registerParameterField(FieldMapping param)
			throws MappingException {
		throw new MappingException(this, "Unexpected parameterization " + param + " for "
				+ this);
	}

	public void beforeMarshal(MarshalContext mc) throws MappingException {
		// default: do nothing
	}

	public void afterUnmarshal(UnmarshalContext mc) throws MappingException {
		// default: do nothing
	}
	
	@Override
	public String toString() {
	  return recordMapping.recordClass.getSimpleName() + "." + field.getName();
	}

  protected void setIntegerFieldValue(MarshalContext mc, FieldMapping field, final long value) throws MappingException {
    try {
      Class<?> t = field.getField().getType();
      if (Integer.class.isAssignableFrom(t) || int.class.isAssignableFrom(t)) {
        if (value > Integer.MAX_VALUE)
          throw new MappingException(field, "Field type Integer is too small to hold the value " + value);
        field.getField().setInt(mc.getRecord(), (int) value);
      } else if (Short.class.isAssignableFrom(t) || short.class.isAssignableFrom(t)) {
        if (value > Short.MAX_VALUE)
          throw new MappingException(field, "Field type Short is too small to hold the value " + value);
        field.getField().setShort(mc.getRecord(), (short) value);
      } else if (Byte.class.isAssignableFrom(t) || byte.class.isAssignableFrom(t)) {
        if (value > Byte.MAX_VALUE)
          throw new MappingException(field, "Field type Byte is too small to hold the value " + value);
        field.getField().setByte(mc.getRecord(), (byte) value);
      } else if (Long.class.isAssignableFrom(t) || long.class.isAssignableFrom(t)) {
        field.getField().setLong(mc.getRecord(), value);
      } else
        throw new MappingException(field, "Field type is not of type long, int, short or byte");
    } catch (Exception e) {
      throw new MappingException(field, "Can't set size/count", e);
    }
  }
}