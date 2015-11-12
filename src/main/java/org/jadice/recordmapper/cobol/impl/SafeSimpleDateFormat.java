package org.jadice.recordmapper.cobol.impl;

import java.text.AttributedCharacterIterator;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

class SafeSimpleDateFormat extends SimpleDateFormat {

  private static ThreadLocal<Map<String, SimpleDateFormat>> formatMap = new ThreadLocal<Map<String, SimpleDateFormat>>();

  private static final long serialVersionUID = 1L;

  public static SimpleDateFormat getFormat(String pattern) {
    final Map<String, SimpleDateFormat> map = getMap();
    SimpleDateFormat format = map.get(pattern);
    if (format == null) {
      format = new SimpleDateFormat(pattern);
      map.put(pattern, format);
    }
    return format;
  }

  private static Map<String, SimpleDateFormat> getMap() {
    Map<String, SimpleDateFormat> map = formatMap.get();
    if (map == null) {
      map = new HashMap<String, SimpleDateFormat>(41);
      formatMap.set(map);
    }
    return map;
  }

  private String pattern;

  @Deprecated
  public SafeSimpleDateFormat() {
    throw new RuntimeException("Not supported");
  }

  public SafeSimpleDateFormat(String pattern) {
    this.pattern = pattern;
  }

  @Deprecated
  public SafeSimpleDateFormat(String pattern, DateFormatSymbols formatSymbols) {
    throw new RuntimeException("Not supported");
  }

  @Deprecated
  public SafeSimpleDateFormat(String pattern, Locale locale) {
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void applyLocalizedPattern(String pattern) {
    // formatMap.get().get(pattern).applyLocalizedPattern(pattern);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void applyPattern(String pattern) {
    // formatMap.get().get(pattern).applyPattern(pattern);
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean equals(Object obj) {
    return getFormat(pattern).equals(obj);
  }

  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
    return getFormat(pattern).format(date, toAppendTo, pos);
  }

  @Override
  public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
    return getFormat(pattern).formatToCharacterIterator(obj);
  }

  @Override
  public Date get2DigitYearStart() {
    return getFormat(pattern).get2DigitYearStart();
  }

  @Override
  public Calendar getCalendar() {
    return getFormat(pattern).getCalendar();
  }

  @Override
  public DateFormatSymbols getDateFormatSymbols() {
    return getFormat(pattern).getDateFormatSymbols();
  }

  @Override
  public NumberFormat getNumberFormat() {
    return getFormat(pattern).getNumberFormat();
  }

  @Override
  public TimeZone getTimeZone() {
    return getFormat(pattern).getTimeZone();
  }

  @Override
  public int hashCode() {
    return getFormat(pattern).hashCode();
  }

  @Override
  public boolean isLenient() {
    return getFormat(pattern).isLenient();
  }

  @Override
  public Date parse(String source) throws ParseException {
    return getFormat(pattern).parse(source);
  }

  @Override
  public Date parse(String text, ParsePosition pos) {
    return getFormat(pattern).parse(text, pos);
  }

  @Override
  public Object parseObject(String source) throws ParseException {
    return getFormat(pattern).parseObject(source);
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {
    return getFormat(pattern).parseObject(source, pos);
  }

  @Override
  @Deprecated
  public void set2DigitYearStart(Date startDate) {
    // getFormat(pattern).set2DigitYearStart(startDate);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void setCalendar(Calendar newCalendar) {
    // getFormat(pattern).setCalendar(newCalendar);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols) {
    // getFormat(pattern).setDateFormatSymbols(newFormatSymbols);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void setLenient(boolean lenient) {
    // getFormat(pattern).setLenient(lenient);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void setNumberFormat(NumberFormat newNumberFormat) {
    // getFormat(pattern).setNumberFormat(newNumberFormat);
    throw new RuntimeException("Not supported");
  }

  @Override
  @Deprecated
  public void setTimeZone(TimeZone zone) {
    // getFormat(pattern).setTimeZone(zone);
    throw new RuntimeException("Not supported");
  }

  @Override
  public String toLocalizedPattern() {
    return getFormat(pattern).toLocalizedPattern();
  }

  @Override
  public String toPattern() {
    return getFormat(pattern).toPattern();
  }

  @Override
  public String toString() {
    return getFormat(pattern).toString();
  }

}
