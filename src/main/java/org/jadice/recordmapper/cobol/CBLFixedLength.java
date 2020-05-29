package org.jadice.recordmapper.cobol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jadice.recordmapper.impl.Auxiliary;

@Target({
    ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Auxiliary
public @interface CBLFixedLength {
  public int value();
}
