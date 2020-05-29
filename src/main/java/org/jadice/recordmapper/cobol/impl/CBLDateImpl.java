package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
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
  private DateTimeFormatter dateTimeFormatter;

  private ZoneId zoneId;
  private SafeSimpleDateFormat dateFormat;

  protected void init(final Annotation a) throws MappingException {
    this.spec = (CBLDate) a;

    if (this.spec.zoneId().length() > 0 && this.spec.zoneOffset().length() > 0)
      throw new MappingException(this, "CBLDate allows either zoneId or zoneOffset, not both at " + this);

    if ((this.spec.zoneId().length() > 0 || this.spec.zoneOffset().length() > 0)
        && !(Instant.class.isAssignableFrom(field.getType())))
      throw new MappingException(this, 
          "Can use zoneId or zoneOffset only in conjunction with a field of type Instant at " + this);

    if (this.spec.zoneId().length() > 0)
      zoneId = this.spec.zoneId().equals(CBLDate.DEFAULT_ZONE) ? ZoneId.systemDefault() : ZoneId.of(this.spec.zoneId());
    if (this.spec.zoneOffset().length() > 0)
      zoneId = ZoneOffset.of(this.spec.zoneOffset());
  }

  public int getSize(final MappingContext ctx) {
    return spec.value().length();
  }

  public void marshal(final MarshalContext ctx, Object value) throws MappingException {
    DateTimeFormatter df = getDateTimeFormatter();

    if (null == value) {
      if (null == spec.nullDate())
        throw new MappingException(this, 
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

    if (temp instanceof Instant) {
      if (null != zoneId) {
        temp = ((Instant) temp).atZone(zoneId);
      } else {
        // use default zone
        temp = ((Instant) temp).atZone(ZoneId.systemDefault());
      }
    }

    ctx.put(df.format(temp));
  }

  public Object unmarshal(final UnmarshalContext ctx) throws MappingException {
    String s = ctx.getString(getSize(ctx));
    if (s.trim().isEmpty())
      return null;

    try {
      Object value;
      Class<?> type = field.getType();
      if (type.isAssignableFrom(TemporalAccessor.class))
        value = getDateTimeFormatter().parse(s);
      else if (type.isAssignableFrom(Instant.class)) {
        value = getDateTimeFormatter().parse(s, Instant::from);
      } else if (type.isAssignableFrom(LocalDateTime.class))
        value = getDateTimeFormatter().parse(s, LocalDateTime::from);
      else if (type.isAssignableFrom(LocalDate.class))
        value = getDateTimeFormatter().parse(s, LocalDate::from);
      else if (type.isAssignableFrom(LocalTime.class))
        value = getDateTimeFormatter().parse(s, LocalTime::from);
      else if (type.isAssignableFrom(OffsetDateTime.class))
        value = getDateTimeFormatter().parse(s, OffsetDateTime::from);
      else if (type.isAssignableFrom(OffsetTime.class))
        value = getDateTimeFormatter().parse(s, OffsetTime::from);
      else if (type.isAssignableFrom(ZonedDateTime.class))
        value = getDateTimeFormatter().parse(s, ZonedDateTime::from);
      else if (type.isAssignableFrom(Date.class))
        value = getDateFormat().parse(s);
      else
        throw new IllegalArgumentException("Don't know how to unmarshal into a " + type);

      return value;
    } catch (DateTimeParseException | ParseException e) {
      throw new MappingException(this, e);
    }
  }

  private DateFormat getDateFormat() {
    if (null == dateFormat)
      dateFormat = new SafeSimpleDateFormat(spec.value());
    return dateFormat;
  }

  private DateTimeFormatter getDateTimeFormatter() {
    if (null == dateTimeFormatter) {
      DateTimeFormatterBuilder b = new DateTimeFormatterBuilder() //
          .parseLenient()
          .appendPattern(spec.value());

      dateTimeFormatter = b.toFormatter();

      if (null != zoneId)
        dateTimeFormatter = dateTimeFormatter.withZone(zoneId);
    }
    return dateTimeFormatter;
  }
}
