package org.jadice.recordmapper.cobol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.UnsupportedTemporalTypeException;

@Target({
    ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface CBLDate {
  public static final String DEFAULT_ZONE = "DEFAULT";

  public String value();

  public String nullDate() default "";

  /**
   * Specify the name of a {@link ZoneId} to use in conjunction with {@link Instant}s when
   * formatting with formats which would otherwise throw a {@link UnsupportedTemporalTypeException}.
   * Valid values are the ones accepted by {@link ZoneId#of(String)} as well as {@value #DEFAULT_ZONE}
   * to use the {@link ZoneId#systemDefault()}.
   */
  public String zoneId() default "";

  /**
   * Specify the name of a {@link ZoneOffset} to use in conjunction with {@link Instant}s when
   * formatting with formats which would otherwise throw a {@link UnsupportedTemporalTypeException}.
   * Valid values are the ones accepted by {@link ZoneOffset#of(String)}.
   */
  public String zoneOffset() default "";
}
