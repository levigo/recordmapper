package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLDate;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLDateImpl extends FieldMapping {
  private CBLDate spec;
  private DateTimeFormatter dateFormat;

  private ZoneId zoneId;

  protected void init(Annotation a) throws MappingException {
    this.spec = (CBLDate) a;

    if (this.spec.zoneId().length() > 0 && this.spec.zoneOffset().length() > 0)
      throw new MappingException("CBLDate allows either zoneId or zoneOffset, not both at " + this);

    if ((this.spec.zoneId().length() > 0 || this.spec.zoneOffset().length() > 0)
        && !(Instant.class.isAssignableFrom(field.getType())))
      throw new MappingException(
          "Can use zoneId or zoneOffset only in conjunction with a field of type Instant at " + this);

    if (this.spec.zoneId().length() > 0)
      zoneId = this.spec.zoneId().equals(CBLDate.DEFAULT_ZONE) ? ZoneId.systemDefault() : ZoneId.of(this.spec.zoneId());
    if (this.spec.zoneOffset().length() > 0)
      zoneId = ZoneOffset.of(this.spec.zoneOffset());
  }

  public int getSize(MappingContext ctx) {
    return spec.value().length();
  }

  public void marshal(MarshalContext ctx, Object value) throws MappingException {
    DateTimeFormatter df = getDateFormat();

    if (null == value) {
      if (null == spec.nullDate())
        throw new MappingException(
            "The field " + field + " must not contain a null date unless its @CBLDate annotation specifies a nullDate");
      if (spec.nullDate().equals("NOW"))
        value = Instant.now();
      else {
        ctx.put(spec.nullDate());
        return;
      }
    }

    TemporalAccessor temp;
    if (value instanceof Date)
      temp = ((Date) value).toInstant();
    else if (value instanceof TemporalAccessor)
      temp = (TemporalAccessor) value;
    else
      throw new IllegalArgumentException("Don't know how to marshal a " + value.getClass());

    if (temp instanceof Instant && null != zoneId) {
      temp = ((Instant) temp).atZone(zoneId);
    }

    ctx.put(df.format(temp));
  }

  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
    String s = ctx.getString(getSize(ctx));
    if (s.trim().isEmpty())
      return null;

    DateTimeFormatter df = getDateFormat();

    try {
      Object value;
      Class<?> type = field.getType();
      if (type.isAssignableFrom(TemporalAccessor.class))
        value = df.parse(s);
      else if (type.isAssignableFrom(Instant.class)) {
        value = df.parse(s, Instant::from);
      } else if (type.isAssignableFrom(LocalDateTime.class))
        value = df.parse(s, LocalDateTime::from);
      else if (type.isAssignableFrom(LocalDate.class))
        value = df.parse(s, LocalDate::from);
      else if (type.isAssignableFrom(LocalTime.class))
        value = df.parse(s, LocalTime::from);
      else if (type.isAssignableFrom(OffsetDateTime.class))
        value = df.parse(s, OffsetDateTime::from);
      else if (type.isAssignableFrom(OffsetTime.class))
        value = df.parse(s, OffsetTime::from);
      else if (type.isAssignableFrom(ZonedDateTime.class))
        value = df.parse(s, ZonedDateTime::from);
      else if (type.isAssignableFrom(Date.class))
        value = Date.from(df.parse(s, Instant::from));
      else
        throw new IllegalArgumentException("Don't know how to unmarshal into a " + type);

      return value;
    } catch (DateTimeParseException e) {
      throw new MappingException(this, e);
    }
  }

  private DateTimeFormatter getDateFormat() {
    if (null == dateFormat) {
      DateTimeFormatterBuilder b = new DateTimeFormatterBuilder() //
          .appendPattern(spec.value());

      dateFormat = b.toFormatter();

      if (null != zoneId)
        dateFormat = dateFormat.withZone(zoneId);
    }
    return dateFormat;
  }
}
