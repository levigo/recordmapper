package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLNumericString;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLNumericStringImpl extends FieldMapping {
  private CBLNumericString spec;
  private String padding;
  private Method valueFactoryMethod;

  @Override
  protected void init(Annotation a) throws MappingException {
    spec = (CBLNumericString) a;

    final char p[] = new char[spec.value()];
    Arrays.fill(p, spec.paddingCharacter());

    padding = new String(p);

    final Class<?> fieldType = field.getType();
    if (!(Number.class.isAssignableFrom(field.getType())
        || fieldType.isPrimitive() && !boolean.class.equals(fieldType)))
      throw new MappingException(
          "The field " + field + " must be numeric in order to be mapped with @CBLNumericString");

    try {
      Class<?> valueType = field.getType();
      if (byte.class.equals(valueType))
        valueType = Byte.class;
      else if (short.class.equals(valueType))
        valueType = Short.class;
      else if (int.class.equals(valueType))
        valueType = Integer.class;
      else if (long.class.equals(valueType))
        valueType = Long.class;
      else if (float.class.equals(valueType))
        valueType = Float.class;
      else if (double.class.equals(valueType))
        valueType = Double.class;

      if (Number.class.isAssignableFrom(valueType))
        valueFactoryMethod = valueType.getMethod("valueOf", String.class);
    } catch (final Exception e) {
      throw new MappingException("The type of the " + field + " doesn't implement the valueOf(String) method.", e);
    }
  }

  @Override
  public int getSize(MappingContext ctx) {
    return spec.value();
  }

  @Override
  public void marshal(MarshalContext ctx, Object value) throws MappingException {
    ctx.put(adjustLength(null != value ? ((Number) value).toString() : "0", ctx));
  }

  private String adjustLength(String value, MappingContext ctx) throws MappingException {
    final int expected = getSize(ctx);
    int actual = value.length();

    if (actual > expected)
      throw new MappingException(
          "Length of value " + value + " for field " + field + " exceeds allowed length(" + expected + ")");

    while (actual < expected) {
      final int pad = Math.min(padding.length(), expected - actual);
      if (spec.alignRight())
        value = padding.substring(0, pad) + value;
      else
        value += padding.substring(0, pad);
      actual += pad;
    }

    return value;
  }

  @Override
  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
    final String s = ctx.getString(getSize(ctx));

    try {
      return valueFactoryMethod.invoke(null, s);
    } catch (final Exception e) {
      throw new MappingException("Can't invoke the field value factory method " + valueFactoryMethod, e);
    }
  }
}
