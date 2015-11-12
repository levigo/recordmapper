package org.jadice.recordmapper.cobol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CBLEnum {
	public int value();

	public char pad() default ' ';
	
	public String nullValue() default "#$#$NULL$#$#";
	
	public String unknownValue() default "#$#$UNKNOWN$#$#";
}
