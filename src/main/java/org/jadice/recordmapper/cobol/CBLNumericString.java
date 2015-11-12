package org.jadice.recordmapper.cobol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CBLNumericString {
	public int value();
	public char paddingCharacter() default '0';
	public boolean alignRight() default true;
	public String format() default "#$#$FORMAT$#$#";
}
