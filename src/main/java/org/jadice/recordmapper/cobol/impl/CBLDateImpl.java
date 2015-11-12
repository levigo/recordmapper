package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLDate;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLDateImpl extends FieldMapping {
  private CBLDate spec;
  private DateFormat dateFormat;

  @Override
  protected void init(Annotation a) {
    spec = (CBLDate) a;
  }

  @Override
  public int getSize(MappingContext ctx) {
    return spec.value().length();
  }

  @Override
  public void marshal(MarshalContext ctx, Object value) throws MappingException {
    final DateFormat df = getDateFormat();

    Date date = (Date) value;
    if (null == date) {
      if (null == spec.nullDate())
        throw new MappingException(
            "The field "
                + field
                + " must not contain a null date unless its @CBLDate annotation specifies a nullDate");
      if (spec.nullDate().equals("NOW")) 
        date = new Date();
      else {
        ctx.put(spec.nullDate());
        return;
      }
    }

    ctx.put(df.format(date));
  }

  @Override
  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
    final String s = ctx.getString(getSize(ctx));
    final DateFormat df = getDateFormat();

    try {
      return df.parse(s);
    } catch (final ParseException e) {
      throw new MappingException(this, e);
    }
  }

  private DateFormat getDateFormat() {
    if (null == dateFormat)
      dateFormat = new SafeSimpleDateFormat(spec.value());
    return dateFormat;
  }
}
