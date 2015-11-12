package org.jadice.recordmapper.cobol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CBLString {
	public int value();

	public char pad() default ' ';
	
	/**
	 * Whether to silently truncate values that are too long.
	 */
	public boolean truncate() default false;
}
