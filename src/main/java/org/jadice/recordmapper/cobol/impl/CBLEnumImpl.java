package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLEnum;
import org.jadice.recordmapper.cobol.CBLEnumValue;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLEnumImpl extends FieldMapping {
  private String padding;

  private CBLEnum spec;

  private final Map<Enum<?>, String> enum2cblValues = new HashMap<Enum<?>, String>();
  private final Map<String, Enum<?>> cblValues2enum = new HashMap<String, Enum<?>>();

  private Enum<?> unknownValue;

  @Override
  @SuppressWarnings({
      "rawtypes", "unchecked"
  })
  protected void init(Annotation a) throws MappingException {
    spec = (CBLEnum) a;

    final char p[] = new char[spec.value()];
    Arrays.fill(p, spec.pad());

    padding = new String(p);

    if (!Enum.class.isAssignableFrom(field.getType()))
      throw new MappingException("The field " + field + " must be an enum type to be mapped using @CBLEnum");

    try {
      final Method valuesM = field.getType().getMethod("values");
      final Object values[] = (Object[]) valuesM.invoke(null);

      for (final Object value : values) {
        String cblValue = ((Enum<?>) value).name();
        final Field enumValueField = field.getType().getField(cblValue);
        final CBLEnumValue v = enumValueField.getAnnotation(CBLEnumValue.class);
        if (null != v) {
          cblValue = v.value();
          enum2cblValues.put((Enum<?>) value, cblValue);
        }

        if (cblValue.length() > spec.value())
          throw new MappingException("The enum value " + cblValue + " is longer than the allocated field length");

        cblValues2enum.put(cblValue, (Enum<?>) value);
      }

      if (!spec.unknownValue().equals("#$#$UNKNOWN$#$#")) {
        if (null != cblValues2enum)
          unknownValue = cblValues2enum.get(spec.unknownValue());
        else
          unknownValue = Enum.valueOf((Class) field.getType(), spec.unknownValue());
        if (null == unknownValue)
          throw new MappingException("The unknown value " + spec.unknownValue()
              + " has no representation in the corresponding enum " + field.getType());
      }
    } catch (final MappingException e) {
      throw e;
    } catch (final Exception e) {
      throw new MappingException("Can't extract enum values from an enum implementing CBLEnumValue", e);
    }
  }

  @Override
  public int getSize(MappingContext ctx) {
    return spec.value();
  }

  @Override
  public void marshal(MarshalContext ctx, Object value) throws MappingException {
    String stringValue;
    if (value == null)
      if (!spec.nullValue().equals("#$#$NULL$#$#"))
        stringValue = spec.nullValue();
      else
        throw new NullPointerException("The field " + field + " must not contain a null value");
    else if (enum2cblValues.containsKey(value))
      stringValue = enum2cblValues.get(value);
    else
      stringValue = ((Enum<?>) value).name();

    ctx.put(adjustLength(stringValue, ctx));
  }

  private String adjustLength(String value, MappingContext ctx) {
    final int expected = getSize(ctx);
    int actual = value.length();

    if (actual > expected)
      return value.substring(0, expected);

    while (actual < expected) {
      final int pad = Math.min(padding.length(), expected - actual);
      value += padding.substring(0, pad);
      actual += pad;
    }

    return value;
  }

  @Override
  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
    final String s = ctx.getString(getSize(ctx)).trim();

    if (!spec.nullValue().equals("#$#$NULL$#$#") && spec.nullValue().equals(s))
      return null;

    Enum<?> enumValue = cblValues2enum.get(s);
    if (null == enumValue)
      if (null != unknownValue)
        enumValue = unknownValue;
      else
        throw new MappingException("The value '" + s
            + "' cannot be mapped to a corresponding enum value and there is no unknown value defined.");

    return enumValue;
  }
}
